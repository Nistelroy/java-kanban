package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;

import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static FileBackedTasksManager getDefault(Path file) {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}