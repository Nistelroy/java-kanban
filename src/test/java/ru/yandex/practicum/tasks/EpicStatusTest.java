package test.java.ru.yandex.practicum.tasks;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testEpicWithoutSubtasksStatusNew() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testEpicAllSubtasksStatusNewEpicStatusNew() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", epic.getId());
        Subtask subtask2 = new Subtask("Имя", "Описание", epic.getId());
        taskManager.setNewSubtask(subtask);
        taskManager.setNewSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "При добавлении 2 сабтаск NEW статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusDoneEpicStatusDone() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", epic.getId());
        Subtask subtask2 = new Subtask("Имя", "Описание", epic.getId());
        taskManager.setNewSubtask(subtask);
        taskManager.setNewSubtask(subtask2);
        subtask.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtask);
        taskManager.updateSubtaskInMap(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "При обновлении 2 сабтаск DONE статус эпика расчитался неправильно");
    }

    @Test
    void testEpicSubtasksStatusDoneAndNewEpicStatusInProgress() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", epic.getId());
        Subtask subtask2 = new Subtask("Имя", "Описание", epic.getId());
        taskManager.setNewSubtask(subtask);
        taskManager.setNewSubtask(subtask2);
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(subtask);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "При обновлении статуса одной сабтаски статус эпика расчитался неправильно");
    }

    @Test
    void testEpicAllSubtasksStatusInProgressEpicStatusInProgress() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", epic.getId());
        Subtask subtask2 = new Subtask("Имя", "Описание", epic.getId());
        taskManager.setNewSubtask(subtask);
        taskManager.setNewSubtask(subtask2);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtask);
        taskManager.updateSubtaskInMap(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "При обновлении 2 сабтаск In Progress статус эпика расчитался неправильно");
    }
}