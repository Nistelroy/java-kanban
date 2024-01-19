package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.http.HttpTaskServer;
import main.java.ru.yandex.practicum.managers.http.KVServer;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private KVServer kvServer;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;
    public static final int INCORRECT_ID = 100;
    public static final int ONE_TASK_IN_LIST = 1;
    public static final int TWO_TASKS_IN_LIST = 2;
    public static final int ANY_DURATION = 30;
    private Task testTask;
    private Epic testEpic;
    private Subtask testSubtask;
    private HttpResponse<String> response;
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = Managers.getDefault();
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();
            httpClient = HttpClient.newHttpClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getTasksForTests();
    }

    @Test
    public void testGetEpicIn1IdReturnCorrectEpic() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Epic epicInServer = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(testEpic, epicInServer);
    }

    @Test
    public void testGetEpicIncorrectIdReturn404() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + INCORRECT_ID);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetTaskIn2IdReturnCorrectTask() {
        taskManager.setNewTask(testTask);

        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Task taskInServer = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(testTask, taskInServer);
    }

    @Test
    public void testGetTaskIncorrectIdReturn404() {

        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + INCORRECT_ID);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetSubtaskIn2IdReturnCorrectSubtask() {
        taskManager.setNewSubtask(testSubtask);

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Subtask subtaskInServer = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode());
        assertEquals(testSubtask, subtaskInServer);
    }

    @Test
    public void testGetSubtaskIncorrectIdReturn404() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + INCORRECT_ID);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testPostNewTaskReturn200() {
        Task testTaskTwo = new Task("Имя для теста", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION));
        String taskJson = gson.toJson(testTaskTwo);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(testTaskTwo.getName(), taskManager.getTaskById(2).getName());
    }

    @Test
    public void testPostNewEpicReturn200() {
        Epic testEpicTwo = new Epic("Имя для теста", "Описание");
        String epicJson = gson.toJson(testEpicTwo);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(testEpicTwo.getName(), taskManager.getEpicById(2).getName());
    }

    @Test
    public void testPostNewSubtaskReturn200() {
        Subtask testSubtaskTwo = new Subtask("Имя для теста", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION * 4), testEpic.getId());
        String subtaskJson = gson.toJson(testSubtaskTwo);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(testSubtaskTwo.getName(), taskManager.getSubtaskById(2).getName());
    }

    @Test
    public void testUpdateTaskReturn200() {
        taskManager.setNewTask(testTask);

        Task updateTask = taskManager.getTaskById(2);
        updateTask.setName("Новое имя");

        String taskJson = gson.toJson(updateTask);
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(updateTask.getName(), taskManager.getTaskById(2).getName());
    }

    @Test
    public void testUpdateEpicReturn200() {

        Epic updateEpic = taskManager.getEpicById(1);
        updateEpic.setName("Новое имя");

        String epicJson = gson.toJson(updateEpic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(updateEpic.getName(), taskManager.getEpicById(1).getName());
    }

    @Test
    public void testUpdateSubtaskReturn200() {
        taskManager.setNewSubtask(testSubtask);

        Subtask updateSubtask = taskManager.getSubtaskById(2);
        updateSubtask.setName("Новое имя");

        String subtaskJson = gson.toJson(updateSubtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.statusCode());
        assertEquals(updateSubtask.getName(), taskManager.getSubtaskById(2).getName());
    }

    @Test
    public void testGetTasksListReturnCorrectList() {
        taskManager.setNewTask(testTask);
        taskManager.setNewTask(new Task("1", "2", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION * 2)));

        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Task> taskList = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getAllTask(), taskList);
    }

    @Test
    public void testGetEpicListReturnCorrectList() {
        taskManager.setNewEpic(new Epic("1", "2"));

        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Epic> epicList = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getAllEpic(), epicList);
    }

    @Test
    public void testGetSubtaskListReturnCorrectList() {
        taskManager.setNewSubtask(testSubtask);
        taskManager.setNewSubtask(new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION * 6), testEpic.getId()));

        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Subtask> subtasksList = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getAllSubtask(), subtasksList);
    }

    @Test
    public void testGetHistoryReturnCorrectHistory() {
        taskManager.getEpicById(1);
        taskManager.setNewTask(testTask);
        taskManager.getTaskById(2);

        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Task> historyInServer = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());

        assertEquals(TWO_TASKS_IN_LIST, historyInServer.size());
    }

    @Test
    public void testGetPrioritizedReturnCorrectPrioritized() {
        taskManager.setNewTask(testTask);

        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Task> prioritizedTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());

        assertEquals(ONE_TASK_IN_LIST, prioritizedTasks.size());
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    protected void getTasksForTests() {
        testTask = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now());
        testEpic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(testEpic);
        testSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION * 3), testEpic.getId());
    }
}
