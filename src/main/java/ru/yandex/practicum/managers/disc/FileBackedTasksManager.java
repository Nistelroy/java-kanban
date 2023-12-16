package main.java.ru.yandex.practicum.managers.disc;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final Path file;

    public FileBackedTasksManager(InMemoryHistoryManager inMemoryHistoryManager, Path file) {
        super(inMemoryHistoryManager);
        this.file = file;
    }

    private void save() {
        List<Task> allTasks = super.getAllTask();
        List<Epic> allEpics = super.getAllEpic();
        List<Subtask> allSubtasks = super.getAllSubtask();
        StringBuilder buffer = new StringBuilder();

        buffer.append("id,type,name,status,description,epic");
        buffer.append(System.lineSeparator());
        for (Task task : allTasks) {
            buffer.append(taskToString(task));
            buffer.append(System.lineSeparator());
        }
        for (Epic epic : allEpics) {
            buffer.append(epicToString(epic));
            buffer.append(System.lineSeparator());
        }
        for (Subtask subtask : allSubtasks) {
            buffer.append(subtaskToString(subtask));
            buffer.append(System.lineSeparator());
        }
        buffer.append(System.lineSeparator());
        buffer.append(historyToString(super.getHistory()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            writer.write(buffer.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Файл data.csv не сохранился", e);
        }
    }

    public void loadFromFile(Path file) {
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            Map<Integer, Task> allTasksForLoadHistory = new HashMap<>();
            for (int i = 1; i < lines.size() - 2; i++) {
                String[] split = lines.get(i).split(",");
                switch (TaskType.valueOf(split[1])) {
                    case TASK:
                        Task task = taskFromString(lines.get(i));
                        allTasksForLoadHistory.put(task.getId(), task);
                        super.updateTaskInMap(task);
                        break;
                    case EPIC:
                        Epic epic = epicFromString(lines.get(i));
                        allTasksForLoadHistory.put(epic.getId(), epic);
                        super.updateEpicInMap(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = subtaskFromString(lines.get(i));
                        epic = (Epic) allTasksForLoadHistory.get(subtask.getIdEpic());
                        epic.setIdSubtask(subtask.getId());
                        allTasksForLoadHistory.put(subtask.getId(), subtask);
                        super.updateSubtaskInMap(subtask);
                }
            }
            List<Integer> historyIdList = historyFromString(lines.get(lines.size() - 1));
            Collections.reverse(historyIdList);
            for (Integer integer : historyIdList) {
                Task task = allTasksForLoadHistory.get(integer);
                if (task.getClass().equals(Task.class)) {
                    super.getTaskById(task.getId());
                } else if (task.getClass().equals(Epic.class)) {
                    super.getEpicById(task.getId());
                } else if (task.getClass().equals(Subtask.class)) {
                    super.getSubtaskById(task.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл data.csv не прочитался", e);
        }
    }

    private String taskToString(Task task) {
        String[] reSplit = new String[5];
        reSplit[0] = String.valueOf(task.getId());
        reSplit[1] = String.valueOf(TaskType.TASK);
        reSplit[2] = task.getName();
        reSplit[3] = String.valueOf(task.getStatus());
        reSplit[4] = task.getDescription();

        return String.join(",", reSplit);
    }

    private String epicToString(Epic epic) {
        String[] reSplit = new String[5];
        reSplit[0] = String.valueOf(epic.getId());
        reSplit[1] = String.valueOf(TaskType.EPIC);
        reSplit[2] = epic.getName();
        reSplit[3] = String.valueOf(epic.getStatus());
        reSplit[4] = epic.getDescription();

        return String.join(",", reSplit);
    }

    private String subtaskToString(Subtask subtask) {
        String[] reSplit = new String[6];
        reSplit[0] = String.valueOf(subtask.getId());
        reSplit[1] = String.valueOf(TaskType.SUBTASK);
        reSplit[2] = subtask.getName();
        reSplit[3] = String.valueOf(subtask.getStatus());
        reSplit[4] = subtask.getDescription();
        reSplit[5] = String.valueOf(subtask.getIdEpic());

        return String.join(",", reSplit);
    }

    private Task taskFromString(String value) {
        String[] split = value.split(",");
        Task task = new Task(split[2], split[4]);
        task.setId(Integer.parseInt(split[0]));
        task.setStatus(TaskStatus.valueOf(split[3]));

        return task;
    }

    private Epic epicFromString(String value) {
        String[] split = value.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setId(Integer.parseInt(split[0]));
        epic.setStatus(TaskStatus.valueOf(split[3]));

        return epic;
    }

    private Subtask subtaskFromString(String value) {
        String[] split = value.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Integer.parseInt(split[5]));
        subtask.setId(Integer.parseInt(split[0]));
        subtask.setStatus(TaskStatus.valueOf(split[3]));

        return subtask;
    }

    private static String historyToString(List<Task> history) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            result.append(history.get(i).getId());
            if (i < history.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> resultList = new ArrayList<>();
        String[] items = value.split(",");
        for (String item : items) {
            resultList.add(Integer.parseInt(item.trim()));
        }
        return resultList;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;

    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;

    }

    @Override
    public void setNewTask(Task task) {
        super.setNewTask(task);
        save();
    }

    @Override
    public void updateTaskInMap(Task task) {
        super.updateTaskInMap(task);
        save();
    }

    @Override
    public void setNewEpic(Epic task) {
        super.setNewEpic(task);
        save();
    }

    @Override
    public void updateEpicInMap(Epic task) {
        super.updateEpicInMap(task);
        save();
    }

    @Override
    public void setNewSubtask(Subtask task) {
        super.setNewSubtask(task);
        save();
    }

    @Override
    public void updateSubtaskInMap(Subtask task) {
        super.updateSubtaskInMap(task);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }
}
