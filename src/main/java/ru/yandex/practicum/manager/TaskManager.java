package main.java.ru.yandex.practicum.manager;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;


import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idForNewTask;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    public Task getTaskById(int id) {
        return tasksMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasksMap.get(id);
    }

    public void setNewTask(Task task) {
        int newId = createId();
        task.setId(newId);
        tasksMap.put(newId, task);
    }

    public void updateTaskInMap(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void setNewEpic(Epic task) {
        int newId = createId();
        task.setId(newId);
        epicMap.put(newId, task);
    }

    public void updateEpicInMap(Epic task) {
        statusEpicChanger(task);
        epicMap.put(task.getId(), task);
    }

    public void setNewSubtask(Subtask task) {
        int newId = createId();
        task.setId(newId);
        epicMap.get(task.getIdEpic()).setIdSubtask(newId);
        subtasksMap.put(newId, task);
    }

    public void updateSubtaskInMap(Subtask task) {
        subtasksMap.put(task.getId(), task);
        Epic thisEpic = epicMap.get(task.getIdEpic());
        statusEpicChanger(thisEpic);
    }

    public void deleteTaskById(Integer id) {
        tasksMap.remove(id);
    }

    public void deleteEpicById(Integer id) {
        for (int i = 0; i < epicMap.get(id).getIdSubtask().size(); i++) {
            subtasksMap.remove(epicMap.get(id).getIdSubtask().get(i));
        }
        epicMap.remove(id);
    }

    public void deleteSubtaskById(Integer id) {
        Integer epicId = subtasksMap.get(id).getIdEpic();
        epicMap.get(epicId).removeEpicSubtasksById(id);
        subtasksMap.remove(id);
        statusEpicChanger(epicMap.get(epicId));
    }

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

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasksMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicMap.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasksMap.values());
    }

    public void deleteAllTask() {
        tasksMap.clear();
    }

    public void deleteAllEpic() {
        epicMap.clear();
        subtasksMap.clear();
    }

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


