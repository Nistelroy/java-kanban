package main;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.TaskStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {
    public static final Path file = Paths.get("src/main/resources/data.csv");

    public static void main(String[] args) {

        TaskManager taskManager = new FileBackedTasksManager(file);
        Epic epic = new Epic("1", "2");
        taskManager.setNewEpic(epic);
        Subtask subtask1 = new Subtask("1", "2", 30, LocalDateTime.now().plusMinutes(30), epic.getId());
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.setNewSubtask(subtask1);
        for (int i = 2; i < 10; i++) {
            Subtask subtask = new Subtask("1" + i, "2", 30, LocalDateTime.now().plusMinutes(30 * i), epic.getId());
            taskManager.setNewSubtask(subtask);
        }

        for (int i = 0; i < taskManager.getPrioritizedTasks().size(); i++) {
            System.out.println(taskManager.getPrioritizedTasks().get(i));
        }
        System.out.println(taskManager.getEpicById(epic.getId()));
        taskManager.deleteAllSubtask();
        System.out.println(taskManager.getEpicById(epic.getId()));

    }
}
