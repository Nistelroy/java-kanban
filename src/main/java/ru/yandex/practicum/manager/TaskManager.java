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

    public void setNewTaskOrSwapInMap(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        } else {
            int newId = createId();
            task.setId(newId);
            tasksMap.put(newId, task);
        }
    }

    public void setNewEpicOrSwapInMap(Epic task) {
        if (epicMap.containsKey(task.getId())) {
            statusEpicChanger(task);
            epicMap.put(task.getId(), task);
        } else {
            int newId = createId();
            task.setId(newId);
            epicMap.put(newId, task);
        }
    }

    public void setNewSubtaskOrSwapInMap(Subtask task) {
        if (subtasksMap.containsKey(task.getId())) {
            subtasksMap.put(task.getId(), task);
            Epic thisEpic = epicMap.get(task.getIdEpic());
            statusEpicChanger(thisEpic);


        } else {
            int newId = createId();
            task.setId(newId);
            epicMap.get(task.getIdEpic()).setIdSubtask(newId);
            subtasksMap.put(newId, task);
        }
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
            thisEpic.setStatus(String.valueOf(ConstantsStatus.NEW));
        }
        subtasksMap.clear();
    }

    private void statusEpicChanger(Epic task) {
        ArrayList<Integer> allSubtaskId = task.getIdSubtask();
        for (Integer integer : allSubtaskId) {
            if (subtasksMap.get(integer).getStatus().equals(String.valueOf(ConstantsStatus.IN_PROGRESS))) {
                task.setStatus(String.valueOf(ConstantsStatus.IN_PROGRESS));
                break;
            }
            if (subtasksMap.get(integer).getStatus().equals(String.valueOf(ConstantsStatus.DONE))) {
                task.setStatus(String.valueOf(ConstantsStatus.DONE));
            }
        }
        if (allSubtaskId.isEmpty()) {
            task.setStatus(String.valueOf(ConstantsStatus.NEW));
        }
    }
}
