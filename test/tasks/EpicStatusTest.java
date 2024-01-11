package tasks;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    private TaskManager taskManager;
    private Epic testEpic;
    private Subtask testSubtask;
    public static final int ANY_DURATION = 30;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        getTasksForTests();
    }

    @Test
    void testEpicWithoutSubtasksStatusNew() {
        taskManager.setNewEpic(testEpic);

        assertEquals(TaskStatus.NEW, testEpic.getStatus());
    }

    @Test
    void testEpicAllSubtasksStatusNewEpicStatusNew() {
        taskManager.setNewEpic(testEpic);
        taskManager.setNewSubtask(testSubtask);
        Subtask subtaskTwo = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        taskManager.setNewSubtask(subtaskTwo);

        assertEquals(TaskStatus.NEW, testEpic.getStatus(), "При добавлении 2 сабтаск NEW статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusDoneEpicStatusDone() {
        taskManager.setNewEpic(testEpic);
        taskManager.setNewSubtask(testSubtask);
        testSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(testSubtask);

        Subtask subtaskTwo = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        taskManager.setNewSubtask(subtaskTwo);
        subtaskTwo.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtaskTwo);

        assertEquals(TaskStatus.DONE, testEpic.getStatus(), "При обновлении 2 сабтаск DONE статус эпика расчитался неправильно");
    }

    @Test
    void testEpicSubtasksStatusDoneAndNewEpicStatusInProgress() {
        taskManager.setNewEpic(testEpic);
        taskManager.setNewSubtask(testSubtask);
        testSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(testSubtask);

        Subtask subtaskTwo = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        taskManager.setNewSubtask(subtaskTwo);


        assertEquals(TaskStatus.IN_PROGRESS, testEpic.getStatus(), "При обновлении статуса одной сабтаски статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusInProgressEpicStatusInProgress() {
        taskManager.setNewEpic(testEpic);
        taskManager.setNewSubtask(testSubtask);
        testSubtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(testSubtask);

        Subtask subtaskTwo = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        taskManager.setNewSubtask(subtaskTwo);
        subtaskTwo.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtaskTwo);

        assertEquals(TaskStatus.IN_PROGRESS, testEpic.getStatus(), "При обновлении 2 сабтаск In Progress статус эпика расчитался неправильно");
    }

    private void getTasksForTests() {
        testEpic = new Epic("Имя", "Описание");
        testEpic.setId(1);
        testSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now(), testEpic.getId());
    }
}