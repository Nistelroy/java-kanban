package main.java.ru.yandex.practicum.managers.memory;

import main.java.ru.yandex.practicum.managers.HistoryManager;
import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import main.java.ru.yandex.practicum.tasks.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private int idForNewTask;
    protected final Map<Integer, Task> tasksMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();
    protected final Map<Integer, Subtask> subtasksMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            int result = o1.getStartTime().compareTo(o2.getStartTime());
            if (result != 0) {
                return result;
            }
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        }
        return Integer.compare(o1.getId(), o2.getId());
    });

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasksMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasksMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void setNewTask(Task task) {
        int newId = createId();
        task.setId(newId);
        tasksMap.put(newId, task);
        updatePrioritizedTasks(task);
    }

    @Override
    public void updateTaskInMap(Task task) {
        tasksMap.put(task.getId(), task);
        updatePrioritizedTasks(task);
    }

    @Override
    public void setNewEpic(Epic task) {
        int newId = createId();
        task.setId(newId);
        epicMap.put(newId, task);
        updatePrioritizedTasks(task);
    }

    @Override
    public void updateEpicInMap(Epic task) {
        statusEpicChanger(task);
        epicMap.put(task.getId(), task);
        updatePrioritizedTasks(task);
    }

    @Override
    public void setNewSubtask(Subtask task) {
        int newId = createId();
        task.setId(newId);
        Epic epic = epicMap.get(task.getIdEpic());
        epic.setIdSubtask(newId);
        subtasksMap.put(newId, task);
        statusEpicChanger(epic);
        updateEpicDurationAndTime(epic);
        updatePrioritizedTasks(task);
    }

    @Override
    public void updateSubtaskInMap(Subtask task) {
        subtasksMap.put(task.getId(), task);
        Epic thisEpic = epicMap.get(task.getIdEpic());
        statusEpicChanger(thisEpic);
        updateEpicDurationAndTime(thisEpic);
        updatePrioritizedTasks(task);
    }

    @Override
    public void deleteTaskById(Integer id) {
        Task task = tasksMap.get(id);
        tasksMap.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(task);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            List<Integer> idSubtasks = epic.getIdSubtask();
            for (Integer idSubtask : idSubtasks) {
                Subtask subtask = subtasksMap.get(idSubtask);
                subtasksMap.remove(idSubtask);
                historyManager.remove(idSubtask);
                prioritizedTasks.remove(subtask);
            }
            epicMap.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(epic);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Epic epic = epicMap.get(subtasksMap.get(id).getIdEpic());
        Subtask subtask = subtasksMap.get(id);
        epic.removeEpicSubtasksById(id);
        subtasksMap.remove(id);
        statusEpicChanger(epic);
        updateEpicDurationAndTime(epic);
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
    }

    @Override
    public List<Subtask> getAllSubtaskFromEpicById(Integer id) {
        List<Subtask> allSubtaskByEpic = new ArrayList<>();
        if (epicMap.containsKey(id)) {
            Epic thisEpic = epicMap.get(id);
            for (Integer subtaskId : thisEpic.getIdSubtask()) {
                allSubtaskByEpic.add(subtasksMap.get(subtaskId));
            }
        }
        return allSubtaskByEpic;
    }

    private int createId() {
        return ++idForNewTask;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public void deleteAllTask() {
        tasksMap.clear();
        for (Task task : historyManager.getHistory()) {
            if (task.getType().equals(TaskType.TASK)) {
                prioritizedTasks.remove(task);
                historyManager.remove(task.getId());
            }
        }
    }

    @Override
    public void deleteAllEpic() {
        for (Epic epic : epicMap.values()) {
            prioritizedTasks.remove(epic);
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasksMap.values()) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        epicMap.clear();
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (Subtask task : subtasksMap.values()) {
            prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        for (Epic thisEpic : epicMap.values()) {
            thisEpic.removeAllSubtasksInEpic();
            thisEpic.setStatus(TaskStatus.NEW);
        }
        subtasksMap.clear();
    }

    private void statusEpicChanger(Epic task) {
        List<Integer> allSubtaskId = task.getIdSubtask();
        if (allSubtaskId.isEmpty()) {
            task.setStatus(TaskStatus.NEW);
        } else if (allSubtaskId.size() == 1) {
            task.setStatus(subtasksMap.get(allSubtaskId.get(0)).getStatus());
        } else {
            List<TaskStatus> allSubtaskStatusByEpic = new ArrayList<>(allSubtaskId.size());
            for (Integer integer : allSubtaskId) {
                allSubtaskStatusByEpic.add(subtasksMap.get(integer).getStatus());
            }
            if (allSubtaskStatusByEpic.contains(TaskStatus.IN_PROGRESS)) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (allSubtaskStatusByEpic.contains(TaskStatus.NEW)) {
                if (allSubtaskStatusByEpic.contains(TaskStatus.DONE)) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                } else task.setStatus(TaskStatus.NEW);
            } else task.setStatus(TaskStatus.DONE);
        }
    }

    private void updateEpicDurationAndTime(Epic task) {
        List<Subtask> subtasks = getAllSubtaskFromEpicById(task.getId());
        if (subtasks.isEmpty()) {
            task.setDuration(0);
            task.setStartTime(null);
            task.setEndTime(null);
        } else {
            int totalDuration = 0;
            LocalDateTime earliestStartTime = null;
            LocalDateTime latestEndTime = null;
            for (Subtask subtask : subtasks) {
                int duration = subtask.getDuration();
                LocalDateTime startTime = subtask.getStartTime();
                LocalDateTime endTime = subtask.getEndTime();
                totalDuration += duration;
                if (earliestStartTime == null || (startTime != null && startTime.isBefore(earliestStartTime))) {
                    earliestStartTime = startTime;
                }
                if (latestEndTime == null || (endTime != null && endTime.isAfter(latestEndTime))) {
                    latestEndTime = endTime;
                }
            }
            task.setDuration(totalDuration);
            task.setStartTime(earliestStartTime);
            task.setEndTime(latestEndTime);
        }
    }

    private void updatePrioritizedTasks(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            prioritizedTasks.remove(task);
            for (Task thisTask : prioritizedTasks) {
                if (tasksIntersect(task, thisTask)) {
                    prioritizedTasks.add(task);
                    throw new IllegalStateException("Периоды задач не могут пересекаться");
                }
            }
            prioritizedTasks.add(task);
        } else if (task.getType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            if (!epic.getIdSubtask().isEmpty()) {
                for (Task existing : prioritizedTasks) {
                    if (tasksIntersect(task, existing)) {
                        throw new IllegalStateException("Периоды задач не могут пересекаться");
                    }
                }
                prioritizedTasks.add(task);
            }
        }
    }

    private boolean tasksIntersect(Task newTask, Task existingTask) {
        LocalDateTime startNew = newTask.getStartTime();
        LocalDateTime endNew = newTask.getEndTime();
        LocalDateTime startExisting = existingTask.getStartTime();
        LocalDateTime endExisting = existingTask.getEndTime();
        if (startNew == null || endNew == null || startExisting == null || endExisting == null) {
            return false;
        }
        return startNew.isBefore(endExisting) && startExisting.isBefore(endNew);
    }
}