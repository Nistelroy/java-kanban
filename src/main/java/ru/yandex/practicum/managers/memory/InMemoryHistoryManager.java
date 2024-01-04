package main.java.ru.yandex.practicum.managers.memory;

import main.java.ru.yandex.practicum.managers.HistoryManager;
import main.java.ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node lastNode;
    private Node firstNode;
    private final Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.remove(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node node = new Node(lastNode, task, null);
        if (lastNode == null) {
            firstNode = node;
        } else {
            lastNode.next = node;
        }
        lastNode = node;
        historyMap.put(task.getId(), node);
    }

    private List<Task> getTasks() {
        List<Task> taskHistoryArrayList = new ArrayList<>();
        Node node = lastNode;
        while (node != null) {
            taskHistoryArrayList.add(node.task);
            node = node.prev;
        }
        return taskHistoryArrayList;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.prev == null && node.next == null) {
                firstNode = null;
                lastNode = null;
            } else {
                if (node.prev != null) {
                    node.prev.next = node.next;
                    if (node.prev.next == null) {
                        lastNode = node.prev;
                    }
                } else {
                    firstNode = node.next;
                }
                if (node.next != null) {
                    node.next.prev = node.prev;
                    if (node.next.prev == null) {
                        firstNode = node.next;
                    }
                } else {
                    lastNode = node.prev;
                }
            }
            node.prev = null;
            node.next = null;
        }
    }
}

class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Node prev, Task task, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}