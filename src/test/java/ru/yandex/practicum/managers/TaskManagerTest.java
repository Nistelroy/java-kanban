package test.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    public static final int INCORRECT_ID = 100;
    public static final int ZERO_TASK_IN_LIST = 0;
    public static final int ONE_TASK_IN_LIST = 1;
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddTaskCorrectTaskInMap() {
        Task task = getNewTask();
        taskManager.setNewTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()));
    }


    @Test
    void testGetTaskIncorrectIdReturnNull() {
        assertNull(taskManager.getTaskById(INCORRECT_ID));
    }

    @Test
    void testAddEpicCorrectEpicInMap() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testGetEpicIncorrectIdReturnNull() {
        assertNull(taskManager.getEpicById(INCORRECT_ID));
    }

    @Test
    void testAddSubtaskCorrectSubtaskInMap() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testAddSubtaskCorrectIdSubtaskInEpic() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(epic.getIdSubtask().get(0), subtask.getId());
    }

    @Test
    void testAddSubtaskCorrectIdEpicInSubtask() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(epic.getId(), taskManager.getSubtaskById(subtask.getId()).getIdEpic());
    }

    @Test
    void testGetSubtaskIncorrectIdReturnNull() {
        assertNull(taskManager.getSubtaskById(INCORRECT_ID));
    }

    @Test
    void testGetHistoryNoGetReturnSizeZero() {
        Task task = getNewTask();
        taskManager.setNewTask(task);

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testGetHistoryOneGetReturnSizeOne() {
        Task task = getNewTask();
        taskManager.setNewTask(task);
        taskManager.getTaskById(task.getId());

        assertEquals(ONE_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testUpdateTaskCorrectTaskInMap() {
        Task task = getNewTask();
        taskManager.setNewTask(task);
        Task newTask = getNewTask();
        newTask.setId(task.getId());
        taskManager.updateTaskInMap(newTask);

        assertEquals(newTask, taskManager.getTaskById(task.getId()));
    }

    @Test
    void testUpdateEpicCorrectEpicInMap() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Epic newEpic = getNewEpic();
        newEpic.setId(epic.getId());
        taskManager.updateEpicInMap(newEpic);

        assertEquals(newEpic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testUpdateSubtaskCorrectSubtaskInMap() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask newSubtask = getNewSubtask(epic);
        newSubtask.setId(subtask.getId());
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(newSubtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testUpdateSubtaskStatusDoneInEpicStatusDone() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask newSubtask = getNewSubtask(epic);
        newSubtask.setId(subtask.getId());
        newSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void testDeleteTaskInMapAbsent() {
        Task task = getNewTask();
        taskManager.setNewTask(task);
        taskManager.deleteTaskById(task.getId());

        assertNull(taskManager.getTaskById(task.getId()));
    }

    @Test
    void testDeleteEpicInMapAbsent() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testDeleteEpicInMapSubtaskAlsoDelete() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskInMapAbsent() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());

        assertNull(taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskIdInEpicAlsoDelete() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());

        assertFalse(epic.getIdSubtask().contains(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskStatusDoneEpicStatusNew() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtask);
        taskManager.deleteSubtaskById(subtask.getId());

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdAddTaskReturnList() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(ONE_TASK_IN_LIST, epic.getIdSubtask().size());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdReturnClearList() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        assertEquals(ZERO_TASK_IN_LIST, epic.getIdSubtask().size());
    }

    @Test
    void testGetAllTaskAddTaskReturnListContTask() {
        Task task = getNewTask();
        taskManager.setNewTask(task);

        assertTrue(taskManager.getAllTask().contains(task));
    }

    @Test
    void testGetAllTaskReturnListNoContTask() {
        Task task = getNewTask();
        assertFalse(taskManager.getAllTask().contains(task));
    }

    @Test
    void testGetAllEpicAddEpicReturnListContEpic() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);

        assertTrue(taskManager.getAllEpic().contains(epic));
    }

    @Test
    void testGetAllEpicReturnListNoContEpic() {
        Epic epic = getNewEpic();
        assertFalse(taskManager.getAllEpic().contains(epic));
    }

    @Test
    void testGetAllSubtaskAddSubtaskReturnListContSubtask() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertTrue(taskManager.getAllSubtask().contains(subtask));
    }

    @Test
    void testGetAllSubtaskReturnListNoContSubtask() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);

        assertFalse(taskManager.getAllSubtask().contains(subtask));
    }

    @Test
    void testDeleteAllSubtaskInMapAbsent() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        Subtask subtaskTwo = getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);
        taskManager.deleteAllSubtask();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllSubtask().size());
    }

    @Test
    void testDeleteAllTaskInMapAbsent() {
        Task task = getNewTask();
        taskManager.setNewTask(task);
        Task taskTwo = getNewTask();
        taskManager.setNewTask(taskTwo);
        taskManager.deleteAllTask();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllTask().size());
    }

    @Test
    void testDeleteAllEpicInMapAbsent() {
        Epic epic = getNewEpic();
        taskManager.setNewEpic(epic);
        Epic epicTwo = getNewEpic();
        taskManager.setNewEpic(epicTwo);
        taskManager.deleteAllEpic();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllEpic().size());
    }

    private static Subtask getNewSubtask(Epic epic) {
        return new Subtask("Имя", "Описание", epic.getId());
    }

    private static Epic getNewEpic() {
        return new Epic("Имя", "Описание");
    }

    private static Task getNewTask() {
        return new Task("Имя", "Описание");
    }
}