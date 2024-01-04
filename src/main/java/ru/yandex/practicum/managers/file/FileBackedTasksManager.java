package main.java.ru.yandex.practicum.managers.file;


import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path file;

    public FileBackedTasksManager(Path file) {
        this.file = file;
    }

    public void save() {
        List<Task> allTasks = getAllTask();
        List<Epic> allEpics = getAllEpic();
        List<Subtask> allSubtasks = getAllSubtask();
        StringBuilder buffer = new StringBuilder();

        buffer.append("id,type,name,status,description,epic");
        buffer.append(System.lineSeparator());
        for (Task task : allTasks) {
            buffer.append(TaskConverter.taskToString(task));
            buffer.append(System.lineSeparator());
        }
        for (Epic epic : allEpics) {
            buffer.append(TaskConverter.epicToString(epic));
            buffer.append(System.lineSeparator());
        }
        for (Subtask subtask : allSubtasks) {
            buffer.append(TaskConverter.subtaskToString(subtask));
            buffer.append(System.lineSeparator());
        }
        buffer.append(System.lineSeparator());
        buffer.append(TaskConverter.historyToString(getHistory()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            writer.write(buffer.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Файл data.csv не сохранился", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(Path file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size() - 2; i++) {
                String[] split = lines.get(i).split(",");
                switch (TaskType.valueOf(split[1])) {
                    case TASK:
                        Task task = TaskConverter.taskFromString(lines.get(i));
                        fileBackedTasksManager.tasksMap.put(task.getId(), task);
                        break;
                    case EPIC:
                        Epic epic = TaskConverter.epicFromString(lines.get(i));
                        fileBackedTasksManager.epicMap.put(epic.getId(), epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = TaskConverter.subtaskFromString(lines.get(i));
                        fileBackedTasksManager.epicMap.get(subtask.getIdEpic()).setIdSubtask(subtask.getId());
                        fileBackedTasksManager.subtasksMap.put(subtask.getId(), subtask);
                }
            }
            if (!(lines.get(lines.size() - 1).isEmpty())) {
                List<Integer> historyIdList = TaskConverter.historyFromString(lines.get(lines.size() - 1));
                for (Integer integer : historyIdList) {
                    if (fileBackedTasksManager.tasksMap.containsKey(integer)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.tasksMap.get(integer));
                    } else if (fileBackedTasksManager.epicMap.containsKey(integer)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicMap.get(integer));
                    } else if (fileBackedTasksManager.subtasksMap.containsKey(integer)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtasksMap.get(integer));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл data.csv не прочитался", e);
        }
        return fileBackedTasksManager;
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
