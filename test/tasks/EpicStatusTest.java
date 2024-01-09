package tasks;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static managers.DataForTests.getNewEpic;
import static managers.DataForTests.getNewSubtask;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testEpicWithoutSubtasksStatusNew() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testEpicAllSubtasksStatusNewEpicStatusNew() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask subtaskTwo = getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "При добавлении 2 сабтаск NEW статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusDoneEpicStatusDone() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtask);

        Subtask subtaskTwo = getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);
        subtaskTwo.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtaskTwo);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "При обновлении 2 сабтаск DONE статус эпика расчитался неправильно");
    }

    @Test
    void testEpicSubtasksStatusDoneAndNewEpicStatusInProgress() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtask);

        Subtask subtaskTwo = getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);


        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "При обновлении статуса одной сабтаски статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusInProgressEpicStatusInProgress() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtask);

        Subtask subtaskTwo = getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);
        subtaskTwo.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtaskTwo);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "При обновлении 2 сабтаск In Progress статус эпика расчитался неправильно");
    }
}