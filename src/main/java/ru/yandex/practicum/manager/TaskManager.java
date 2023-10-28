package main.java.ru.yandex.practicum.manager;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;


import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idForNewTask; // изначально подумал, что статик поможет в последствии перекидывать задачи из разных менеджеров между собой)
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
        epicMap.get(subtasksMap.get(id).getIdEpic()).removeEpicSubtasksById(id);
        subtasksMap.remove(id);
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
        for (Integer integer : allSubtaskId) {
            if (subtasksMap.get(integer).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                task.setStatus(TaskStatus.IN_PROGRESS);
                break;
            }
            if (subtasksMap.get(integer).getStatus().equals(TaskStatus.DONE)) {
                task.setStatus(TaskStatus.DONE);
            }
        }
        if (allSubtaskId.isEmpty()) {
            task.setStatus(TaskStatus.NEW);
        }
    }
}
