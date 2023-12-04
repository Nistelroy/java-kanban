package main.java.ru.yandex.practicum.managers;

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
//            Node node = historyMap.remove(task.getId()); не совсем понял как перенести его в removeNode, если для вызова removeNode нужна node
//            removeNode(node);
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
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
                lastNode = null;
                firstNode = null;
                return;
            }
            Node leftNode = node.prev;
            Node rightNode = node.next;
            if (leftNode != null) {
                leftNode.next = rightNode;
                if (leftNode.prev == null) {
                    firstNode = leftNode;
                }
            }
            if (rightNode != null) {
                rightNode.prev = leftNode;
                if (rightNode.next == null) {
                    lastNode = rightNode;
                }
            }
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