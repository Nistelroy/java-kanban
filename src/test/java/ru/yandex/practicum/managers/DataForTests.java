package test.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataForTests {
    public static final Path file = Paths.get("src/main/resources/test_data.csv");
    public static final int INCORRECT_ID = 100;
    public static final int ZERO_TASK_IN_LIST = 0;
    public static final int ONE_TASK_IN_LIST = 1;


    public static Subtask getNewSubtask(Epic epic) {
        return new Subtask("Имя", "Описание", epic.getId());
    }

    public static Epic getNewEpic() {
        return new Epic("Имя", "Описание");
    }

    public static Task getNewTask() {
        return new Task("Имя", "Описание");
    }
}
