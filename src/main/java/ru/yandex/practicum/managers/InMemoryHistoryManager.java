package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_HISTORY_SIZE = 10;
    private final List<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() == MAX_HISTORY_SIZE) {
                historyList.remove(0);
            }
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}