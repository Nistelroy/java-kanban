package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idForNewTask;
    private final Map<Integer, Task> tasksMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, Subtask> subtasksMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();  // cначала хотел сделать List.copyOf, но он всё равно давал ссылки на таски в мапе, пошёл по дорогому пути создания полной копии)
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasksMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasksMap.get(id);
        historyManager.add(subtask);
        return subtask;
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
    public List<Subtask> getAllSubtaskFromEpicById(Integer id) {
        List<Subtask> allSubtaskByEpic = new ArrayList<>();
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
    public List<Task> getAllTask() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
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
        List<Integer> allSubtaskId = task.getIdSubtask();
        if (allSubtaskId.isEmpty()) {
            task.setStatus(TaskStatus.NEW);
        } else if (allSubtaskId.size() == 1) {
            task.setStatus(subtasksMap.get(allSubtaskId.get(0)).getStatus());
        } else {
            List<TaskStatus> allSubtaskStatusByEpic = new ArrayList<>(allSubtaskId.size());
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


