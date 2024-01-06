package main.java.ru.yandex.practicum.managers.file;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import main.java.ru.yandex.practicum.tasks.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskConverter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String taskToString(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(TaskType.TASK),
                task.getName(),
                String.valueOf(task.getStatus()),
                task.getDescription(),
                String.valueOf(task.getDuration()),
                task.getStartTime() == null ? "" : FORMATTER.format(task.getStartTime())
        );
    }

    public static String epicToString(Epic epic) {
        return String.join(",",
                String.valueOf(epic.getId()),
                String.valueOf(TaskType.EPIC),
                epic.getName(),
                String.valueOf(epic.getStatus()),
                epic.getDescription(),
                String.valueOf(epic.getDuration()),
                epic.getStartTime() == null ? "" : FORMATTER.format(epic.getStartTime())
        );
    }

    public static String subtaskToString(Subtask subtask) {
        return String.join(",",
                String.valueOf(subtask.getId()),
                String.valueOf(TaskType.SUBTASK),
                subtask.getName(),
                String.valueOf(subtask.getStatus()),
                subtask.getDescription(),
                String.valueOf(subtask.getIdEpic()),
                String.valueOf(subtask.getDuration()),
                subtask.getStartTime() == null ? "" : FORMATTER.format(subtask.getStartTime())
        );
    }

    public static Task taskFromString(String value) {
        String[] split = value.split(",");
        Task task = new Task(split[2], split[4], Integer.parseInt(split[5]),
                parseDateTime(split[6]));
        task.setId(Integer.parseInt(split[0]));
        task.setStatus(TaskStatus.valueOf(split[3]));
        return task;
    }

    public static Epic epicFromString(String value) {
        String[] split = value.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setId(Integer.parseInt(split[0]));
        epic.setStatus(TaskStatus.valueOf(split[3]));
        epic.setDuration(Integer.parseInt(split[5]));
        if (split.length > 6) {
            epic.setStartTime(parseDateTime(split[6]));
        }
        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] split = value.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Integer.parseInt(split[6]),
                parseDateTime(split[7]), Integer.parseInt(split[5]));
        subtask.setId(Integer.parseInt(split[0]));
        subtask.setStatus(TaskStatus.valueOf(split[3]));
        return subtask;
    }

    public static String historyToString(List<Task> history) {
        if (history != null) {
            StringBuilder result = new StringBuilder();
            for (int i = history.size() - 1; i >= 0; i--) {
                result.append(history.get(i).getId());
                if (i > 0) {
                    result.append(",");
                }
            }
            return result.toString();
        }
        return "";
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> resultList = new ArrayList<>();
        String[] items = value.split(",");
        for (String item : items) {
            resultList.add(Integer.parseInt(item.trim()));
        }
        return resultList;
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        return dateTime.isEmpty() ? null : LocalDateTime.parse(dateTime, FORMATTER);
    }
}
