package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    public static final Path file = Paths.get("src/main/resources/data.csv");

    private Managers() {
    }

    public static FileBackedTasksManager getDefault(Path thisFile) {
        return new FileBackedTasksManager(thisFile);
    }

    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}