package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.manager.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.setNewTask(task);
        taskManager.setNewTask(task);
        taskManager.setNewTask(Epic);

        taskManager.setNewTask(Epic);

        System.out.println(taskManager.map);
        System.out.println(taskManager.map1);
        System.out.println(taskManager.map2);

        taskManager.updateTaskById();
        taskManager.updateTaskById();

        taskManager.getAllTasks();

        taskManager.deleteTaskById();
        taskManager.deleteTaskById();


    }
}
