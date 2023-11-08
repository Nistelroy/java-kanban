package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idForNewTask;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasksMap.get(id));
        return tasksMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasksMap.get(id));
        return subtasksMap.get(id);
    }

    @Override
    public void setNewTask(Task task) {
        int newId = createId();
        task.setId(newId);
        tasksMap.put(newId, task);
    }

    @Override
    public void updateTaskInMap(Task task) {
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void setNewEpic(Epic task) {
        int newId = createId();
        task.setId(newId);
        epicMap.put(newId, task);
    }

    @Override
    public void updateEpicInMap(Epic task) {
        statusEpicChanger(task);
        epicMap.put(task.getId(), task);
    }

    @Override
    public void setNewSubtask(Subtask task) {
        int newId = createId();
        task.setId(newId);
        epicMap.get(task.getIdEpic()).setIdSubtask(newId);
        subtasksMap.put(newId, task);
    }

    @Override
    public void updateSubtaskInMap(Subtask task) {
        subtasksMap.put(task.getId(), task);
        Epic thisEpic = epicMap.get(task.getIdEpic());
        statusEpicChanger(thisEpic);
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasksMap.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        for (int i = 0; i < epicMap.get(id).getIdSubtask().size(); i++) {
            subtasksMap.remove(epicMap.get(id).getIdSubtask().get(i));
        }
        epicMap.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Integer epicId = subtasksMap.get(id).getIdEpic();
        epicMap.get(epicId).removeEpicSubtasksById(id);
        subtasksMap.remove(id);
        statusEpicChanger(epicMap.get(epicId));
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskFromEpicById(Integer id) {
        ArrayList<Subtask> allSubtaskByEpic = new ArrayList<>();
        if (epicMap.containsKey(id)) {
            Epic thisEpic = epicMap.get(id);
            for (Integer subtaskId : thisEpic.getIdSubtask()) {
                allSubtaskByEpic.add(subtasksMap.get(subtaskId));
            }
        }
        return allSubtaskByEpic;
    }

    private int createId() {
        return ++idForNewTask;
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public void deleteAllTask() {
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicMap.clear();
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (Epic thisEpic : epicMap.values()) {
            thisEpic.removeAllSubtasksInEpic();
            thisEpic.setStatus(TaskStatus.NEW);
        }
        subtasksMap.clear();
    }

    private void statusEpicChanger(Epic task) {
        ArrayList<Integer> allSubtaskId = task.getIdSubtask();
        if (allSubtaskId.isEmpty()) {
            task.setStatus(TaskStatus.NEW);
        } else if (allSubtaskId.size() == 1) {
            task.setStatus(subtasksMap.get(allSubtaskId.get(0)).getStatus());
        } else {
            ArrayList<TaskStatus> allSubtaskStatusByEpic = new ArrayList<>(allSubtaskId.size());
            for (Integer integer : allSubtaskId) {
                allSubtaskStatusByEpic.add(subtasksMap.get(integer).getStatus());
            }
            if (allSubtaskStatusByEpic.contains(TaskStatus.IN_PROGRESS)) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (allSubtaskStatusByEpic.contains(TaskStatus.NEW)) {
                if (allSubtaskStatusByEpic.contains(TaskStatus.DONE)) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else task.setStatus(TaskStatus.NEW);
            } else task.setStatus(TaskStatus.DONE);
        }
    }
}


