package managers;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.http.HttpTaskServer;
import main.java.ru.yandex.practicum.managers.http.KVServer;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    private KVServer kvServer;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;
    public static final int INCORRECT_ID = 100;
    public static final int ZERO_TASK_IN_LIST = 0;
    public static final int ONE_TASK_IN_LIST = 1;
    public static final int TWO_TASKS_IN_LIST = 2;
    public static final int ANY_DURATION = 30;
    private Task testTask;
    private Epic testEpic;
    private Subtask testSubtask;
    @BeforeEach
    void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = Managers.getDefault();
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        getTasksForTests();
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
        testSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION*3), testEpic.getId());
    }
}
