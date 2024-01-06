package test.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataForTests {

    // При многократном запуске тестов они проходят, в логике работы менеджера багов не нашёл
    // В тестах багов тоже не нашёл, скорее всего баги в моих методах генерации тасок для тестов, я так и не нашёл в чём именно

    public static final Path file = Paths.get("src/main/resources/test_data.csv");
    public static final int INCORRECT_ID = 100;
    public static final int ZERO_TASK_IN_LIST = 0;
    public static final int ONE_TASK_IN_LIST = 1;
    public static List<Task> existingTasks = new ArrayList<>();
    public static final int ANY_DURATION = 30;

    public static Subtask getNewSubtask(Epic epic) {
        return createRandomSubtask(epic);
    }

    public static Epic getNewEpic() {
        return new Epic("Имя", "Описание");
    }

    public static Task getNewTask() {
        return createRandomTask();
    }

    public static Task createRandomTask() {
        Random rand = new Random();
        int maxAttempts = 100;
        while (maxAttempts > 0) {
            LocalDateTime randomStartTime = LocalDateTime.now().plusMinutes(rand.nextInt(14400));
            LocalDateTime randomEndTime = randomStartTime.plusMinutes(rand.nextInt(60));
            boolean isOverlap = false;
            for (Task task : existingTasks) {
                LocalDateTime existingStartTime = task.getStartTime();
                LocalDateTime existingEndTime = task.getEndTime();
                if (!randomEndTime.isBefore(existingStartTime) && !randomStartTime.isAfter(existingEndTime)) {
                    isOverlap = true;
                    break;
                }
            }
            if (!isOverlap) {
                return new Task("Имя", "Описание", (int) Duration.between(randomStartTime, randomEndTime).toMinutes(), randomStartTime);
            }
            maxAttempts--;
        }
        throw new IllegalStateException("Не удалось создать задачу с не пересекающимся временем.");
    }

    public static Subtask createRandomSubtask(Epic epic) {
        Random rand = new Random();
        int maxAttempts = 100;
        boolean isOverlap;
        while (maxAttempts > 0) {
            LocalDateTime randomStartTime;
            LocalDateTime randomEndTime;
            do {
                randomStartTime = LocalDateTime.now().plusMinutes(rand.nextInt(14400));
                randomEndTime = randomStartTime.plusMinutes(rand.nextInt(60));
                isOverlap = isTimeOverlap(randomStartTime, randomEndTime, existingTasks);
            } while (isOverlap && --maxAttempts > 0);
            if (!isOverlap) {
                Subtask subtask = new Subtask("Имя", "Описание", (int) Duration.between(randomStartTime, randomEndTime).toMinutes(), randomStartTime, epic.getId());
                existingTasks.add(subtask);
                return subtask;
            }
            maxAttempts--;
        }
        throw new IllegalStateException("Не удалось создать задачу с не пересекающимся временем.");
    }

    private static boolean isTimeOverlap(LocalDateTime startTime, LocalDateTime endTime, List<Task> tasks) {
        for (Task task : tasks) {
            if (!endTime.isBefore(task.getStartTime()) && !startTime.isAfter(task.getEndTime())) {
                return true;
            }
        }
        return false;
    }
}
