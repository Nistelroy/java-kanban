package main.java.ru.yandex.practicum.managers.file;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import main.java.ru.yandex.practicum.tasks.TaskType;

public class TaskConverter {
    public static String taskToString(Task task) {
        String[] reSplit = new String[5];
        reSplit[0] = String.valueOf(task.getId());
        reSplit[1] = String.valueOf(TaskType.TASK);
        reSplit[2] = task.getName();
        reSplit[3] = String.valueOf(task.getStatus());
        reSplit[4] = task.getDescription();

        return String.join(",", reSplit);
    }

    public static String epicToString(Epic epic) {
        String[] reSplit = new String[5];
        reSplit[0] = String.valueOf(epic.getId());
        reSplit[1] = String.valueOf(TaskType.EPIC);
        reSplit[2] = epic.getName();
        reSplit[3] = String.valueOf(epic.getStatus());
        reSplit[4] = epic.getDescription();

        return String.join(",", reSplit);
    }

    public static String subtaskToString(Subtask subtask) {
        String[] reSplit = new String[6];
        reSplit[0] = String.valueOf(subtask.getId());
        reSplit[1] = String.valueOf(TaskType.SUBTASK);
        reSplit[2] = subtask.getName();
        reSplit[3] = String.valueOf(subtask.getStatus());
        reSplit[4] = subtask.getDescription();
        reSplit[5] = String.valueOf(subtask.getIdEpic());

        return String.join(",", reSplit);
    }

    public static Task taskFromString(String value) {
        String[] split = value.split(",");
        Task task = new Task(split[2], split[4]);
        task.setId(Integer.parseInt(split[0]));
        task.setStatus(TaskStatus.valueOf(split[3]));

        return task;
    }

    public static Epic epicFromString(String value) {
        String[] split = value.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setId(Integer.parseInt(split[0]));
        epic.setStatus(TaskStatus.valueOf(split[3]));

        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] split = value.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Integer.parseInt(split[5]));
        subtask.setId(Integer.parseInt(split[0]));
        subtask.setStatus(TaskStatus.valueOf(split[3]));

        return subtask;
    }
}
