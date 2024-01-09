package managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(DataForTests.file);
    }

    @Test
    void testHistoryBackUpInFileClearHistory() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(DataForTests.file);

        List<Task> history1 = taskManager.getHistory();
        List<Task> history2 = taskManager2.getHistory();

        assertEquals(history1, history2);
    }

    @Test
    void testHistoryBackUpInFileFullHistory() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());

        addFifeRequestsInHistory();

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(DataForTests.file);

        List<Task> history1 = taskManager.getHistory();
        List<Task> history2 = taskManager2.getHistory();

        assertEquals(history1, history2);
    }

    @Test
    void testHistoryBackUpInFileClearTaskMap() {
        taskManager.save();

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(DataForTests.file);

        List<Task> history1 = taskManager.getHistory();
        List<Task> history2 = taskManager2.getHistory();

        assertEquals(history1, history2);
    }
}