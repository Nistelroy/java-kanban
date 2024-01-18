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

import static main.java.ru.yandex.practicum.tasks.TaskType.EPIC;
import static main.java.ru.yandex.practicum.tasks.TaskType.SUBTASK;
import static main.java.ru.yandex.practicum.tasks.TaskType.TASK;


public class TaskHandler implements HttpHandler {
    private static final String HISTORY = "history";
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
        String methodRequest = h.getRequestMethod();
        URI requestURI = h.getRequestURI();
        String path = requestURI.getPath();
        String[] splitPath = path.split("/");

        if (splitPath.length == 2 && methodRequest.equals("GET")) {
            handleGetPriorList(h);
        }
        switch (methodRequest) {
            case "POST":
                if (splitPath[2].equals(TASK.toString())) {
                    handleSetOrUpdateTask(h);
                } else if (splitPath[2].equals(EPIC.toString())) {
                    handleSetOrUpdateEpic(h);
                } else if (splitPath[2].equals(SUBTASK.toString())) {
                    handleSetOrUpdateSubtask(h);
                } else {
                    output(h, "Страница не найдена", 404);
                }
                break;
            case "GET":
                if (splitPath[2].equals(TASK.toString())) {
                    handleGetTaskInMap(h);
                } else if (splitPath[2].equals(EPIC.toString())) {
                    handleGetEpicInMap(h);
                } else if (splitPath[2].equals(SUBTASK.toString())) {
                    handleGetSubtaskInMap(h);
                } else if (splitPath[2].equals(HISTORY)) {
                    handleGetHistoryList(h);
                } else {
                    output(h, "Страница не найдена", 404);
                }
                break;
            case "DELETE":
                if (splitPath[2].equals(TASK.toString())) {
                    handleDeleteTask(h);
                } else if (splitPath[2].equals(EPIC.toString())) {
                    handleDeleteEpic(h);
                } else if (splitPath[2].equals(SUBTASK.toString())) {
                    handleDeleteSubTask(h);
                } else {
                    output(h, "Страница не найдена", 404);
                }
                break;
            default:
                output(h, "Неизвестный запрос", 405);
        }
    }

    public void handleGetTaskInMap(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int idTask = getId(h);
            if (taskManager.getTaskById(idTask) != null) {
                Task task = taskManager.getTaskById(idTask);
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
            int idEpic = getId(h);
            if (taskManager.getEpicById(idEpic) != null) {
                Epic epic = taskManager.getEpicById(idEpic);
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
            int idSubtask = getId(h);
            if (taskManager.getSubtaskById(idSubtask) != null) {
                Subtask subTask = taskManager.getSubtaskById(idSubtask);
                output(h, gson.toJson(subTask), 200);
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
        Integer idTask = task.getId();
        if (idTask == null) {
            taskManager.setNewTask(task);
            output(h, "Создали новую таску", 200);
        } else {
            if (taskManager.getTaskById(idTask) != null) {
                taskManager.updateTaskInMap(task);
                output(h, "Обновили таску", 200);
            } else {
                output(h, "Для создания новой такски, она должна быть без id", 404);
            }
        }
    }

    public void handleSetOrUpdateEpic(HttpExchange h) throws IOException {
        String body = readBodyText(h);
        if (body.isEmpty()) {
            output(h, "Ничего не передано.", 400);
            return;
        }
        Epic epic = gson.fromJson(body, Epic.class);
        Integer idEpic = epic.getId();
        if (idEpic == null) {
            taskManager.setNewEpic(epic);
            output(h, "Создали новый эпик", 200);
        } else {
            if (taskManager.getEpicById(idEpic) != null) {
                taskManager.updateEpicInMap(epic);
                output(h, "Обновили эпик", 200);
            } else {
                output(h, "Для создания нового эпика, она должна быть без id", 404);
            }
        }
    }

    public void handleSetOrUpdateSubtask(HttpExchange h) throws IOException {
        String body = readBodyText(h);
        if (body.isEmpty()) {
            output(h, "Ничего не передано.", 400);
            return;
        }
        Subtask subtask = gson.fromJson(body, Subtask.class);
        Integer idSubtask = subtask.getId();
        int idEpic = subtask.getIdEpic();
        if (idSubtask == null) {
            if (taskManager.getEpicById(idEpic) != null) {
                taskManager.setNewSubtask(subtask);
                output(h, "Создали новую сабтаску", 200);
            } else {
                output(h, "Эпика для сабтаски нет", 404);
            }
        } else {
            if (taskManager.getSubtaskById(idSubtask) != null) {
                taskManager.updateSubtaskInMap(subtask);
                output(h, "Обновили сабтаску", 200);
            } else {
                output(h, "Для создания новой сабтаски, она должна быть без id", 404);
            }
        }
    }

    public void handleDeleteTask(HttpExchange h) throws IOException {
        if (h.getRequestURI().getQuery() != null) {
            int idTask = getId(h);
            if (taskManager.getTaskById(idTask) != null) {
                taskManager.deleteTaskById(idTask);
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
            int idEpic = getId(h);
            if (taskManager.getEpicById(idEpic) != null) {
                taskManager.deleteEpicById(idEpic);
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
            int idSubtask = getId(h);
            if (taskManager.getSubtaskById(idSubtask) != null) {
                taskManager.deleteSubtaskById(idSubtask);
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
        return Integer.parseInt
                (h
                        .getRequestURI()
                        .toString()
                        .split("\\?")[1]
                        .split("=")[1]);
    }
}