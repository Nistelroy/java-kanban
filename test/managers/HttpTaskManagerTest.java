package managers;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.http.HttpTaskManager;
import main.java.ru.yandex.practicum.managers.http.HttpTaskServer;
import main.java.ru.yandex.practicum.managers.http.KVServer;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private static KVServer kvServer;
    private HttpTaskManager httpTaskManager;
    private HttpTaskServer httpTaskServer;

    @Override
    protected HttpTaskManager createTaskManager() {
        return Managers.getDefault();
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void setUp() {
        try {
            taskManager = createTaskManager();
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        getTasksForTests();
    }

    @Test
    void testBackUpInServerFullTasks() {
        taskManager.setNewTask(testTask);
        taskManager.setNewSubtask(testSubtask);
        taskManager.getTaskById(testTask.getId());
        taskManager.getEpicById(testEpic.getId());
        taskManager.getSubtaskById(testSubtask.getId());

        List<Task> tasks = taskManager.getAllTask();
        List<Epic> epics = taskManager.getAllEpic();
        List<Subtask> subtasks = taskManager.getAllSubtask();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritized = taskManager.getPrioritizedTasks();

        httpTaskManager = new HttpTaskManager("http://localhost:8078", true);

        assertEquals(tasks, httpTaskManager.getAllTask());
        assertEquals(epics, httpTaskManager.getAllEpic());
        assertEquals(subtasks, httpTaskManager.getAllSubtask());
        assertEquals(history, httpTaskManager.getHistory());
        assertEquals(prioritized, httpTaskManager.getPrioritizedTasks());
    }

    @Test
    void testBackUpInServerFullTasksClearHistory() {
        taskManager.setNewTask(testTask);
        taskManager.setNewSubtask(testSubtask);

        List<Task> tasks = taskManager.getAllTask();
        List<Epic> epics = taskManager.getAllEpic();
        List<Subtask> subtasks = taskManager.getAllSubtask();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritized = taskManager.getPrioritizedTasks();

        httpTaskManager = new HttpTaskManager("http://localhost:8078", true);

        assertEquals(tasks, httpTaskManager.getAllTask());
        assertEquals(epics, httpTaskManager.getAllEpic());
        assertEquals(subtasks, httpTaskManager.getAllSubtask());
        assertEquals(history, httpTaskManager.getHistory());
        assertEquals(prioritized, httpTaskManager.getPrioritizedTasks());
    }

    @Test
    void testBackUpInServerZeroTasks() {
        taskManager.deleteAllEpic();

        List<Task> tasks = taskManager.getAllTask();
        List<Epic> epics = taskManager.getAllEpic();
        List<Subtask> subtasks = taskManager.getAllSubtask();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritized = taskManager.getPrioritizedTasks();

        httpTaskManager = new HttpTaskManager("http://localhost:8078", true);

        assertEquals(tasks, httpTaskManager.getAllTask());
        assertEquals(epics, httpTaskManager.getAllEpic());
        assertEquals(subtasks, httpTaskManager.getAllSubtask());
        assertEquals(history, httpTaskManager.getHistory());
        assertEquals(prioritized, httpTaskManager.getPrioritizedTasks());
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }
}



