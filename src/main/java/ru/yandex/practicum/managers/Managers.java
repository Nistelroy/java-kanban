package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryHistoryManagerForTest() {
        Map<Integer, Task> tasksMap = new HashMap<>();
        Map<Integer, Epic> epicMap = new HashMap<>();
        Map<Integer, Subtask> subtasksMap = new HashMap<>();

        fillingMap(tasksMap, epicMap, subtasksMap);

        return new InMemoryTaskManager(tasksMap, epicMap, subtasksMap);
    }

    private static void fillingMap(Map<Integer, Task> tasksMap, Map<Integer, Epic> epicMap, Map<Integer, Subtask> subtasksMap) {
        Subtask subtask;
        Epic epic;
        Task task;
        ArrayList<Integer> subtaskId;

        //создаем две таски
        task = new Task("купить", "купить-купить", 1, TaskStatus.NEW);
        tasksMap.put(1, task);
        task = new Task("найти", "найти-найти", 2, TaskStatus.NEW);
        tasksMap.put(2, task);

        //создаём эпик и 2 субтаски
        subtaskId = new ArrayList<>();
        subtaskId.add(4);
        subtaskId.add(5);

        epic = new Epic("Поездка", "Поездка-Поездка", 3, TaskStatus.NEW, subtaskId);
        epicMap.put(3, epic);
        subtask = new Subtask("машина", "машина-машина", 4, TaskStatus.NEW, 3);
        subtasksMap.put(4, subtask);
        subtask = new Subtask("бензин", "бензин-бензин", 5, TaskStatus.NEW, 3);
        subtasksMap.put(5, subtask);

        subtaskId = new ArrayList<>();
        subtaskId.add(7);

        //создаем эпик и субтаску
        epic = new Epic("Прогулка", "Прогулка-Прогулка", 6, TaskStatus.NEW, subtaskId);
        epicMap.put(6, epic);
        subtask = new Subtask("еда", "еда-еда", 7, TaskStatus.NEW, 6);
        subtasksMap.put(7, subtask);
    }
}
