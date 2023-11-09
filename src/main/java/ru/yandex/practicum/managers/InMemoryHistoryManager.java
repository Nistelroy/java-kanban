package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_HISTORY_SIZE = 10;
    private final List<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == MAX_HISTORY_SIZE) {
            historyList.remove(0);
        }
        if (!(task == null)) {
            if (task.getClass().equals(Task.class)) {
                Task taskCopy = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());
                historyList.add(taskCopy);
            } else if (task.getClass().equals(Epic.class)) {
                Epic taskCopy = new Epic(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                        ((Epic) task).getIdSubtask());
                historyList.add(taskCopy);
            } else if (task.getClass().equals(Subtask.class)) {
                Subtask taskCopy = new Subtask(task.getName(), task.getDescription(), task.getId(),
                        task.getStatus(), ((Subtask) task).getIdEpic());
                historyList.add(taskCopy);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
