package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}