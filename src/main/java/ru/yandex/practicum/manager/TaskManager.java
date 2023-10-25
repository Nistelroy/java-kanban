package main.java.ru.yandex.practicum.manager;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int idForNewTask;
    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

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

    public void setNewEpic(Epic task) {
        int newId = createId();
        task.setId(newId);
        epicMap.put(newId, task);
    }

    public void setNewSubtask(Subtask task) {
        int newId = createId();
        task.setId(newId);
        epicMap.get(task.getIdEpic()).setIdSubtask(newId);
        subtasksMap.put(newId, task);
    }

    public void changeTaskById(int id, String status) {
        Task newTask = tasksMap.get(id);
        newTask.setStatus(status);
        tasksMap.put(id, newTask);
    }

    public void changeSubtaskById(int id, String status) {
        Subtask newSubtask = subtasksMap.get(id);
        newSubtask.setStatus(status);

        Epic newEpic = epicMap.get(newSubtask.getIdEpic());
        for (int i = 0; i < newEpic.getIdSubtask().size(); i++) {

            if (subtasksMap.get(newEpic.getIdSubtask().get(i)).getStatus().equals("IN_PROGRESS")) {
                newEpic.setStatus("IN_PROGRESS");
                break;
            }
            if (subtasksMap.get(newEpic.getIdSubtask().get(i)).getStatus().equals("DONE")) {
                newEpic.setStatus("DONE");
            }
        }
        subtasksMap.put(id, newSubtask);
    }

    public void deleteTaskById(int id) {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
        } else if (epicMap.containsKey(id)) {
            for (int i = 0; i < epicMap.get(id).getIdSubtask().size(); i++) {
                subtasksMap.remove(epicMap.get(id).getIdSubtask().get(i));
            }
            epicMap.remove(id);
        } else if (subtasksMap.containsKey(id)) {
            epicMap.get(subtasksMap.get(id).getIdEpic()).removeIdSubtask(id);
            subtasksMap.remove(id);
        }
    }

    public void replacementTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void replacementEpic(Epic task) {
        epicMap.put(task.getId(), task);
    }

    public void replacementSubtask(Subtask task) {
        subtasksMap.put(task.getId(), task);
    }

    public ArrayList<Subtask> getAllSubtaskFromEpicById(int id) {
        if (epicMap.containsKey(id)) {
            ArrayList<Subtask> arrayList = new ArrayList<>();
            for (int i = 0; i < epicMap.get(id).getIdSubtask().size(); i++) {
                arrayList.add(subtasksMap.get(epicMap.get(id).getIdSubtask().get(i)));
            }
            return arrayList;
        }
        return null;
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
}
