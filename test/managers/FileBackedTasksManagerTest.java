package managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(file);
    }

    @Test
    void testHistoryBackUpInFileClearHistory() {

        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), taskManager2.getHistory());
        assertEquals(taskManager.getAllTask(), taskManager2.getAllTask());
        assertEquals(taskManager.getAllSubtask(), taskManager2.getAllSubtask());
        assertEquals(taskManager.getAllEpic(), taskManager2.getAllEpic());
        assertEquals(taskManager.getPrioritizedTasks(), taskManager2.getPrioritizedTasks());
    }

    @Test
    void testHistoryBackUpInFileFullHistory() {
        for (int i = 0; i < 5; i++) {
            Epic epic = new Epic("Имя", "Описание");
            taskManager.setNewEpic(epic);
            taskManager.getEpicById(epic.getId());
        }

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), taskManager2.getHistory());
        assertEquals(taskManager.getAllTask(), taskManager2.getAllTask());
        assertEquals(taskManager.getAllSubtask(), taskManager2.getAllSubtask());
        assertEquals(taskManager.getAllEpic(), taskManager2.getAllEpic());
        assertEquals(taskManager.getPrioritizedTasks(), taskManager2.getPrioritizedTasks());
    }

    @Test
    void testHistoryBackUpInFileClearTaskMap() {
        taskManager.save();

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), taskManager2.getHistory());
        assertEquals(taskManager.getAllTask(), taskManager2.getAllTask());
        assertEquals(taskManager.getAllSubtask(), taskManager2.getAllSubtask());
        assertEquals(taskManager.getAllEpic(), taskManager2.getAllEpic());
        assertEquals(taskManager.getPrioritizedTasks(), taskManager2.getPrioritizedTasks());
    }
}