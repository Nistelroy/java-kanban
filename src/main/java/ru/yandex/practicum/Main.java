package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.manager.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import static main.java.ru.yandex.practicum.manager.ConstantsStatus.*;

public class Main {

    public static void main(String[] args) {
        Task task;
        Epic epic;
        Subtask subtask;
        TaskManager taskManager = new TaskManager();

        task = new Task("купить", "купить-купить");
        taskManager.setTaskInMap(task);
        task = new Task("найти", "найти-найти");
        taskManager.setTaskInMap(task);

        epic = new Epic("Поездка", "Поездка-Поездка");
        taskManager.setEpicInMap(epic);
        subtask = new Subtask("машина", "машина-машина", epic.getId());
        taskManager.setSubtaskInMap(subtask);
        subtask = new Subtask("бензин", "бензин-бензин", epic.getId());
        taskManager.setSubtaskInMap(subtask);

        epic = new Epic("Прогулка", "Прогулка-Прогулка");
        taskManager.setEpicInMap(epic);
        subtask = new Subtask("еда", "еда-еда", epic.getId());
        taskManager.setSubtaskInMap(subtask);

        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic() + "\n" + taskManager.getAllSubtask() + "\n");

        task = new Task("Другая", "Другая-Другая");
        task.setStatus(IN_PROGRESS);
        task.setId(1);
        taskManager.setTaskInMap(task);

        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(IN_PROGRESS);
        subtask.setId(5);
        taskManager.setSubtaskInMap(subtask);

        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(DONE);
        subtask.setId(4);
        taskManager.setSubtaskInMap(subtask);

        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic() + "\n" + taskManager.getAllSubtask() + "\n");

        epic = new Epic("Новая", "Новая-Новая");
        epic.setIdSubtask(4);
        epic.setIdSubtask(5);
        epic.setId(3);
        taskManager.setEpicInMap(epic);

        subtask = new Subtask("Свежая", "Свежая-Свежая", 6);
        subtask.setStatus(IN_PROGRESS);
        subtask.setId(7);
        taskManager.setSubtaskInMap(subtask);

        subtask = new Subtask("Уходящая", "Уходящая-Уходящая", 3);
        subtask.setStatus(DONE);
        subtask.setId(5);
        taskManager.setSubtaskInMap(subtask);

        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic() + "\n" + taskManager.getAllSubtask() + "\n");

        taskManager.deleteTaskById(2);
        taskManager.deleteTaskById(3);

        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic() + "\n" + taskManager.getAllSubtask() + "\n");
    }
}
