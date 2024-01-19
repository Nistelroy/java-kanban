package main.java.ru.yandex.practicum.managers.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();
    public static final Path file = Paths.get("src/main/resources/data.csv");

    public HttpTaskManager(String url) {
        super(file);
        kvTaskClient = new KVTaskClient(url);

    }

    public HttpTaskManager(String url, String anyfing) {
        super(file);
        kvTaskClient = new KVTaskClient(url);
        loadingServer();
    }

    @Override
    public void save() {
        String jsonAllTasks = gson.toJson(getAllTask());
        String jsonAllEpics = gson.toJson(getAllEpic());
        String jsonAllSubtasks = gson.toJson(getAllSubtask());
        String jsonAllHistory = gson
                .toJson(getHistory()
                        .stream()
                        .map(Task::getId)
                        .collect(Collectors.toList()));

        kvTaskClient.put("tasks", jsonAllTasks);
        kvTaskClient.put("epics", jsonAllEpics);
        kvTaskClient.put("subtasks", jsonAllSubtasks);
        kvTaskClient.put("history", jsonAllHistory);
    }

    private void loadingServer() {
        String jsonTasks = kvTaskClient.load("tasks");
        ArrayList<Task> taskList = gson.fromJson(jsonTasks, new TypeToken<ArrayList<Task>>() {
        }.getType());

        for (Task task : taskList) {
            tasksMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        }

        String jsonEpics = kvTaskClient.load("epics");
        ArrayList<Epic> epicList = gson.fromJson(jsonEpics, new TypeToken<ArrayList<Epic>>() {
        }.getType());

        for (Epic epic : epicList) {
            epicMap.put(epic.getId(), epic);
        }

        String jsonSubtasks = kvTaskClient.load("subtasks");
        ArrayList<Subtask> subtaskList = gson.fromJson(jsonSubtasks, new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        for (Subtask subtask : subtaskList) {
            Epic epic = epicMap.get(subtask.getIdEpic());
            updatePrioritizedTasks(subtask);
            subtasksMap.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            statusEpicChanger(epic);
            updateEpicDurationAndTime(epic);
        }

        String jsonHistory = kvTaskClient.load("history");
        ArrayList<Integer> taskHistoryIds = gson.fromJson(jsonHistory, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        Collections.reverse(taskHistoryIds);

        for (Integer id : taskHistoryIds) {
            if (tasksMap.containsKey(id)) {
                historyManager.add(getTaskById(id));
            }
            if (epicMap.containsKey(id)) {
                historyManager.add(getEpicById(id));
            }
            if (subtasksMap.containsKey(id)) {
                historyManager.add(getSubtaskById(id));
            }
        }
    }
}