package main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node lastNode;
    private final Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyMap.get(task.getId()) != null) {
                removeNode(historyMap.get(task.getId()));
                historyMap.remove(task.getId());
            }
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
        if (lastNode == null) {
            Node node = new Node(null, task, null);
            historyMap.put(task.getId(), node);
            lastNode = node;
        } else {
            Node node = new Node(lastNode, task, null);
            lastNode.next = node;
            lastNode = node;
            historyMap.put(task.getId(), node);
        }
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
            if ((historyMap.size() == 1) && (historyMap.containsKey(node.task.getId()))) {
                historyMap.clear();
                lastNode = null;
                node.prev = null;
                node.next = null;
                return;
            }

            Node leftNode = node.prev;
            Node rightNode = node.next;
            if (leftNode != null) {
                leftNode.next = rightNode;
            }
            if (rightNode != null) {
                rightNode.prev = leftNode;
            }
            node.prev = null;
            node.next = null;
        }
    }
}