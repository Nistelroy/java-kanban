package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.http.HttpTaskManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;

public class Managers {

    private Managers() {
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}