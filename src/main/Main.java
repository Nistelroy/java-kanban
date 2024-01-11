package main;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {
    public static final Path file = Paths.get("src/main/resources/data.csv");

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager(file);
        Epic epic = new Epic("1", "2");
        taskManager.setNewEpic(epic);
        for (int i = 1; i < 10; i++) {
            Task task = new Task("1" + i, "2", 30, LocalDateTime.now().plusMinutes(30 * i));
            taskManager.setNewTask(task);
        }

        for (int i = 0; i < taskManager.getPrioritizedTasks().size(); i++) {
            System.out.println(taskManager.getPrioritizedTasks().get(i));
        }
        Subtask subtask = new Subtask("1", "2", 30, LocalDateTime.now().minusMinutes(400), epic.getId());
        taskManager.setNewSubtask(subtask);
        System.out.println();

        for (int i = 0; i < taskManager.getPrioritizedTasks().size(); i++) {
            System.out.println(taskManager.getPrioritizedTasks().get(i));
        }
        System.out.println(taskManager.getEpicById(epic.getId()));


    }
}
