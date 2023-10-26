package main.java.ru.yandex.practicum.manager;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import static main.java.ru.yandex.practicum.manager.ConstantsStatus.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int idForNewTask;
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

    public void setTaskInMap(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        } else {
            int newId = createId();
            task.setId(newId);
            tasksMap.put(newId, task);
        }
    }

    public void setEpicInMap(Epic task) {
        if (epicMap.containsKey(task.getId())) {
            ArrayList<Integer> allSubtaskId = task.getIdSubtask();

            for (Integer integer : allSubtaskId) {
                if (subtasksMap.get(integer).getStatus().equals(IN_PROGRESS)) {
                    task.setStatus(IN_PROGRESS);
                    break;
                }
                if (subtasksMap.get(integer).getStatus().equals(DONE)) {
                    task.setStatus(DONE);
                }
            }

            epicMap.put(task.getId(), task);
        } else {
            int newId = createId();
            task.setId(newId);
            epicMap.put(newId, task);
        }
    }

    public void setSubtaskInMap(Subtask task) {
        if (subtasksMap.containsKey(task.getId())) {
            subtasksMap.put(task.getId(), task);
            Epic epic = epicMap.get(task.getIdEpic());
            ArrayList<Integer> allSubtaskId = epic.getIdSubtask();
            for (Integer integer : allSubtaskId) {

                if (subtasksMap.get(integer).getStatus().equals(IN_PROGRESS)) {
                    epic.setStatus(IN_PROGRESS);
                    break;
                }
                if (subtasksMap.get(integer).getStatus().equals(DONE)) {
                    epic.setStatus(DONE);
                }
            }

        } else {
            int newId = createId();
            task.setId(newId);
            epicMap.get(task.getIdEpic()).setIdSubtask(newId);
            subtasksMap.put(newId, task);
        }
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
            epicMap.get(subtasksMap.get(id).getIdEpic()).removeEpicSubtasks(id);
            subtasksMap.remove(id);
        }
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
