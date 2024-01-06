package test.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test.java.ru.yandex.practicum.managers.DataForTests.file;
import static test.java.ru.yandex.practicum.managers.DataForTests.getNewEpic;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(file);
    }

    @Test
    void testHistoryBackUpInFileClearHistory() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertFalse(taskManager.getHistory().contains(epic));
    }

    @Test
    void testHistoryBackUpInFileFullHistory() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());

        addFifeRequestsInHistory();

        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertTrue(taskManager.getHistory().contains(epic));
    }

    @Test
    void testHistoryBackUpInFileClearTaskMap() {
        Epic epic = getNewEpic();

        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertFalse(taskManager.getHistory().contains(epic));
    }

    @AfterEach
    void tearDown() {
        taskManager = new FileBackedTasksManager(file);
        taskManager.save();
    }
}