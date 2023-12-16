package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.disc.FileBackedTasksManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    private static final Path file = Paths.get("src/main/resources/data.csv");

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileManager() {
        return new FileBackedTasksManager(new InMemoryHistoryManager(), file);
    }


}