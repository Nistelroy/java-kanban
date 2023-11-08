package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void setNewTask(Task task);

    void updateTaskInMap(Task task);

    void setNewEpic(Epic task);

    void updateEpicInMap(Epic task);

    void setNewSubtask(Subtask task);

    void updateSubtaskInMap(Subtask task);

    void deleteTaskById(Integer id);

    void deleteEpicById(Integer id);

    void deleteSubtaskById(Integer id);

    ArrayList<Subtask> getAllSubtaskFromEpicById(Integer id);

    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubtask();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();
}
