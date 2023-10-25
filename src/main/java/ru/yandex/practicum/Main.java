package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.manager.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {
        Task task;
        Epic epic;
        Subtask subtask;
        TaskManager taskManager = new TaskManager();

        task = new Task("купить", "купить-купить");
        taskManager.setNewTask(task);
        task = new Task("найти", "найти-найти");
        taskManager.setNewTask(task);

        epic = new Epic("Поездка", "Поездка-Поездка");
        taskManager.setNewEpic(epic);
        subtask = new Subtask("машина", "машина-машина", epic.getId());
        taskManager.setNewSubtask(subtask);
        subtask = new Subtask("бензин", "бензин-бензин", epic.getId());
        taskManager.setNewSubtask(subtask);

        epic = new Epic("Прогулка", "Прогулка-Прогулка");
        taskManager.setNewEpic(epic);
        subtask = new Subtask("еда", "еда-еда", epic.getId());
        taskManager.setNewSubtask(subtask);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask() + "\n");

        taskManager.changeTaskById(1, "DONE");
        taskManager.changeTaskById(2, "IN_PROGRESS");
        taskManager.changeSubtaskById(4, "IN_PROGRESS");
        taskManager.changeSubtaskById(5, "DONE");
        taskManager.changeSubtaskById(7, "IN_PROGRESS");

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask() + "\n");

        taskManager.deleteTaskById(2);
        taskManager.deleteTaskById(3);

        taskManager.changeSubtaskById(7, "DONE");

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
    }
}
