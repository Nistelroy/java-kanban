package main.java.ru.yandex.practicum.managers.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String metodRequest = h.getRequestMethod();
        URI requestURL = h.getRequestURI();
        String path = requestURL.getPath();
        String[] splitPath = path.split("/");

        if (splitPath.length == 2 && metodRequest.equals("GET")) {
            handleGetPriorList(h);
        }
        switch (metodRequest) {
            case "POST":
                switch (splitPath[2]) {
                    case "task":
                        handleSetOrUpdateTask(h);
                        break;
                    case "epic":
                        handleSetOrUpdateEpic(h);
                        break;
                    case "subtask":
                        handleSetOrUpdateSubtask(h);
                        break;
                    default:
                        output(h, "Страница не найдена", 404);
                        break;
                }
                break;
            case "GET":
                switch (splitPath[2]) {
                    case "task":
                        handleGetTaskInMap(h);
                        break;
                    case "epic":
                        handleGetEpicInMap(h);
                        break;
                    case "subtask":
                        handleGetSubtaskInMap(h);
                        break;
                    case "history":
                        handleGetHistoryList(h);
                        break;
                    default:
                        output(h, "Страница не найдена", 404);
                        break;
                }
                break;
            case "DELETE":
                switch (splitPath[2]) {
                    case "task":
                        handleDeleteTask(h);
                        break;
                    case "epic":
                        handleDeleteEpic(h);
                        break;
                    case "subtask":
                        handleDeleteSubTask(h);
                        break;
                    default:
                        output(h, "Страница не найдена", 404);
                        break;
                }
                break;
            default:
                output(h, "Неизвестный запрос", 405);
        }
    }

    public void handleGetTaskInMap(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int taskId = getId(h);
            if (taskManager.getTaskById(taskId) != null) {
                Task task = taskManager.getTaskById(taskId);
                output(h, gson.toJson(task), 200);
            } else {
                output(h, "Таска не найдена", 404);
            }
        } else {
            if (!(taskManager.getAllTask().isEmpty())) {
                output(h, gson.toJson(taskManager.getAllTask()), 200);
            } else {
                output(h, "Список тасок пуст", 404);
            }
        }
    }

    public void handleGetEpicInMap(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int epicId = getId(h);
            if (taskManager.getEpicById(epicId) != null) {
                Epic epic = taskManager.getEpicById(epicId);
                output(h, gson.toJson(epic), 200);
            } else {
                output(h, "Эпик не найден", 404);
            }
        } else {
            if (!(taskManager.getAllEpic().isEmpty())) {
                output(h, gson.toJson(taskManager.getAllEpic()), 200);
            } else {
                output(h, "Список эпиков пуст", 404);
            }
        }
    }

    public void handleGetSubtaskInMap(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int subtaskId = getId(h);
            if (taskManager.getSubtaskById(subtaskId) != null) {
                Subtask subtask = taskManager.getSubtaskById(subtaskId);
                output(h, gson.toJson(subtask), 200);
            } else {
                output(h, "Сабтаска не найдена", 404);
            }
        } else {
            if (!(taskManager.getAllSubtask().isEmpty())) {
                output(h, gson.toJson(taskManager.getAllSubtask()), 200);
            } else {
                output(h, "Список сабтасок пуст", 404);
            }
        }
    }

    public void handleSetOrUpdateTask(HttpExchange h) throws IOException {
        String body = readBodyText(h);
        if (body.isEmpty()) {
            output(h, "Ничего не передано.", 400);
            return;
        }
        Task task = gson.fromJson(body, Task.class);
        if (taskManager.getTaskById(task.getId()) != null) {
            taskManager.updateTaskInMap(task);
            output(h, "Обновили таску", 200);
        } else {
            taskManager.setNewTask(task);
            output(h, "Создали новую таску", 200);
        }
    }

    public void handleSetOrUpdateEpic(HttpExchange h) throws IOException {
        String body = readBodyText(h);
        if (body.isEmpty()) {
            output(h, "Ничего не передано.", 400);
            return;
        }
        Epic epic = gson.fromJson(body, Epic.class);
        if (taskManager.getEpicById(epic.getId()) != null) {
            taskManager.updateEpicInMap(epic);
            output(h, "Обновили эпик", 200);
        } else {
            taskManager.setNewEpic(epic);
            output(h, "Создали новый эпик", 200);
        }
    }

    public void handleSetOrUpdateSubtask(HttpExchange h) throws IOException {
        String body = readBodyText(h);
        if (body.isEmpty()) {
            output(h, "Ничего не передано.", 400);
            return;
        }
        Subtask subtask = gson.fromJson(body, Subtask.class);
        if (taskManager.getSubtaskById(subtask.getId()) != null) {
            taskManager.updateSubtaskInMap(subtask);
            output(h, "Обновили сабтаску", 200);
        } else {
            taskManager.setNewSubtask(subtask);
            output(h, "Создали новую сабтаску", 200);
        }
    }


    public void handleDeleteTask(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int taskId = getId(h);
            if (taskManager.getTaskById(taskId) != null) {
                taskManager.deleteTaskById(taskId);
                output(h, "Удалили таску", 200);
            } else {
                output(h, "Таска для удаления не найдена", 404);
            }
        } else {
            handleDeleteClearAllMap(h);
        }
    }

    public void handleDeleteEpic(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int epicId = getId(h);
            if (taskManager.getEpicById(epicId) != null) {
                taskManager.deleteEpicById(epicId);
                output(h, "Удалили эпик", 200);
            } else {
                output(h, "Эпик для удаления не найден", 404);
            }
        } else {
            handleDeleteClearAllMap(h);
        }
    }

    public void handleDeleteSubTask(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int subtaskId = getId(h);
            if (taskManager.getSubtaskById(subtaskId) != null) {
                taskManager.deleteSubtaskById(subtaskId);
                output(h, "Удалили субтаску", 200);
            } else {
                output(h, "Субтаска для удаления не найдена", 404);
            }
        } else {
            handleDeleteClearAllMap(h);
        }
    }

    public void handleDeleteClearAllMap(HttpExchange h) throws IOException {
        taskManager.deleteAllTask();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubtask();
        output(h, "Все задачи удалены.", 200);

    }

    public void handleGetHistoryList(HttpExchange h) throws IOException {
        if (!(taskManager.getHistory().isEmpty())) {
            output(h, gson.toJson(taskManager.getHistory()), 200);
        } else {
            output(h, "Истории нет", 404);
        }
    }

    public void handleGetPriorList(HttpExchange h) throws IOException {
        if (!(taskManager.getPrioritizedTasks().isEmpty())) {
            output(h, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } else {
            output(h, "Нет списка задач", 404);
        }
    }

    private void output(HttpExchange h, String response, int code) throws IOException {
        h.sendResponseHeaders(code, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String readBodyText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private int getId(HttpExchange h) {
        return Integer.parseInt(h.getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1]);
    }
}