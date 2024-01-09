package managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    public static final int FIFE_TASK_IN_HISTORY = 5;

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddTaskCorrectTaskInMap() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void testGetTaskIncorrectIdReturnNull() {
        assertNull(taskManager.getTaskById(DataForTests.INCORRECT_ID));
    }

    @Test
    void testAddEpicCorrectEpicInMap() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testGetEpicIncorrectIdReturnNull() {
        assertNull(taskManager.getEpicById(DataForTests.INCORRECT_ID));
    }

    @Test
    void testAddSubtaskCorrectSubtaskInMap() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testAddSubtaskCorrectIdSubtaskInEpic() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(epic.getIdSubtask().get(0), subtask.getId());
    }

    @Test
    void testAddSubtaskCorrectIdEpicInSubtask() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(epic.getId(), taskManager.getSubtaskById(subtask.getId()).getIdEpic());
    }

    @Test
    void testGetSubtaskIncorrectIdReturnNull() {
        assertNull(taskManager.getSubtaskById(DataForTests.INCORRECT_ID));
    }

    @Test
    void testGetHistoryNoGetReturnSizeZero() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testGetHistoryOneGetReturnSizeOne() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        taskManager.getTaskById(task.getId());

        assertEquals(DataForTests.ONE_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testUpdateTaskCorrectTaskInMap() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        Task newTask = DataForTests.getNewTask();
        newTask.setId(task.getId());
        taskManager.updateTaskInMap(newTask);

        assertEquals(newTask, taskManager.getTaskById(task.getId()));
    }

    @Test
    void testUpdateEpicCorrectEpicInMap() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Epic newEpic = DataForTests.getNewEpic();
        newEpic.setId(epic.getId());
        taskManager.updateEpicInMap(newEpic);

        assertEquals(newEpic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testUpdateSubtaskCorrectSubtaskInMap() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask newSubtask = DataForTests.getNewSubtask(epic);
        newSubtask.setId(subtask.getId());
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(newSubtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testUpdateSubtaskStatusDoneInEpicStatusDone() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask newSubtask = DataForTests.getNewSubtask(epic);
        newSubtask.setId(subtask.getId());
        newSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void testDeleteTaskInMapAbsent() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);
        taskManager.deleteTaskById(task.getId());

        assertNull(taskManager.getTaskById(task.getId()));
    }

    @Test
    void testDeleteEpicInMapAbsent() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testDeleteEpicInMapSubtaskAlsoDelete() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskInMapAbsent() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());

        assertNull(taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskIdInEpicAlsoDelete() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());

        assertFalse(epic.getIdSubtask().contains(subtask.getId()));
    }

    @Test
    void testDeleteSubtaskStatusDoneEpicStatusNew() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdAddTaskReturnList() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertEquals(DataForTests.ONE_TASK_IN_LIST, epic.getIdSubtask().size());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdReturnClearList() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, epic.getIdSubtask().size());
    }

    @Test
    void testGetAllTaskAddTaskReturnListContTask() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        assertTrue(taskManager.getAllTask().contains(task));
    }

    @Test
    void testGetAllTaskReturnListNoContTask() {
        Task task = DataForTests.getNewTask();

        assertFalse(taskManager.getAllTask().contains(task));
    }

    @Test
    void testGetAllEpicAddEpicReturnListContEpic() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        assertTrue(taskManager.getAllEpic().contains(epic));
    }

    @Test
    void testGetAllEpicReturnListNoContEpic() {
        Epic epic = DataForTests.getNewEpic();

        assertFalse(taskManager.getAllEpic().contains(epic));
    }

    @Test
    void testGetAllSubtaskAddSubtaskReturnListContSubtask() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        assertTrue(taskManager.getAllSubtask().contains(subtask));
    }

    @Test
    void testGetAllSubtaskReturnListNoContSubtask() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);

        assertFalse(taskManager.getAllSubtask().contains(subtask));
    }

    @Test
    void testDeleteAllSubtaskInMapAbsent() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Subtask subtask = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtask);

        Subtask subtaskTwo = DataForTests.getNewSubtask(epic);
        taskManager.setNewSubtask(subtaskTwo);

        taskManager.deleteAllSubtask();

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, taskManager.getAllSubtask().size());
    }

    @Test
    void testDeleteAllTaskInMapAbsent() {
        Task task = DataForTests.getNewTask();
        taskManager.setNewTask(task);

        Task taskTwo = DataForTests.getNewTask();
        taskManager.setNewTask(taskTwo);

        taskManager.deleteAllTask();

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, taskManager.getAllTask().size());
    }

    @Test
    void testDeleteAllEpicInMapAbsent() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Epic epicTwo = DataForTests.getNewEpic();
        taskManager.setNewEpic(epicTwo);

        taskManager.deleteAllEpic();

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, taskManager.getAllEpic().size());
    }

    @Test
    void testHistoryZeroGetEpicHistoryClear() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);

        Epic epicTwo = DataForTests.getNewEpic();
        taskManager.setNewEpic(epicTwo);

        assertEquals(DataForTests.ZERO_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testHistoryFifeGetEpicHistoryFull() {
        addFifeRequestsInHistory();

        assertEquals(FIFE_TASK_IN_HISTORY, taskManager.getHistory().size());
    }

    @Test
    void testHistoryRepeatGetEpicHistoryLastEpic() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());

        addFifeRequestsInHistory();

        taskManager.getEpicById(epic.getId());

        assertEquals(epic, taskManager.getHistory().get(0));
    }

    @Test
    void testHistoryDeleteEpicInStartHistoryContFalse() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());

        addFifeRequestsInHistory();

        taskManager.deleteEpicById(epic.getId());

        assertFalse(taskManager.getHistory().contains(epic));
    }

    @Test
    void testHistoryDeleteEpicInМiddleHistoryContFalse() {
        addFifeRequestsInHistory();
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());
        addFifeRequestsInHistory();

        taskManager.deleteEpicById(epic.getId());

        assertFalse(taskManager.getHistory().contains(epic));
    }

    @Test
    void testHistoryDeleteEpicInEndHistoryContFalse() {
        addFifeRequestsInHistory();
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        taskManager.getEpicById(epic.getId());

        taskManager.deleteEpicById(epic.getId());

        assertFalse(taskManager.getHistory().contains(epic));
    }

    @Test
    void getStartTimeTaskReturnStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Имя", "Описание", DataForTests.ANY_DURATION, startTime);
        taskManager.setNewTask(task);
        Task taskInMap = taskManager.getTaskById(task.getId());
        assertEquals(startTime, taskInMap.getStartTime());
    }

    @Test
    void getEndTimeTaskReturnEndTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Имя", "Описание", DataForTests.ANY_DURATION, startTime);
        LocalDateTime endTime = startTime.plusMinutes(DataForTests.ANY_DURATION);
        taskManager.setNewTask(task);
        Task taskInMap = taskManager.getTaskById(task.getId());
        assertEquals(endTime, taskInMap.getEndTime());
    }

    @Test
    void getEndTaskTimeShouldBeNullWithoutStartTime() {
        Task taskWithoutStart = new Task("Имя", "Описание", DataForTests.ANY_DURATION, null);
        taskManager.setNewTask(taskWithoutStart);
        Task taskInMap = taskManager.getTaskById(taskWithoutStart.getId());
        assertNull(taskInMap.getEndTime());
    }

    @Test
    void getEpicStartTimeWithoutSubtasks() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        Epic epicInMap = taskManager.getEpicById(epic.getId());
        assertNull(epicInMap.getStartTime());
    }


    @Test
    void getEpicEndTimeWithoutSubtasks() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        Epic epicInMap = taskManager.getEpicById(epic.getId());
        assertNull(epicInMap.getStartTime());
    }

    @Test
    void getSubtaskStartTimeShouldBeNullWithoutStartTime() {
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", DataForTests.ANY_DURATION, null, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtaskInMap = taskManager.getSubtaskById(subtask.getId());

        assertNull(subtaskInMap.getEndTime());
        assertNull(epic.getEndTime());
    }

    @Test
    void getSubtaskStartTimeReturnStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", DataForTests.ANY_DURATION, startTime, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtaskInMap = taskManager.getSubtaskById(subtask.getId());

        assertEquals(startTime, subtaskInMap.getStartTime());
        assertEquals(startTime, epic.getStartTime());
        assertEquals(startTime.plusMinutes(DataForTests.ANY_DURATION), subtaskInMap.getEndTime());
        assertEquals(startTime.plusMinutes(DataForTests.ANY_DURATION), epic.getEndTime());
    }


    @Test
    void getEpicEndTimeTwoSubtasksSummDuration() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic = DataForTests.getNewEpic();
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", DataForTests.ANY_DURATION, startTime, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Имя", "Описание", DataForTests.ANY_DURATION, startTime.plusMinutes(DataForTests.ANY_DURATION), epic.getId());
        taskManager.setNewSubtask(subtask2);

        Epic epicInMap = taskManager.getEpicById(epic.getId());

        assertEquals(startTime.plusMinutes(DataForTests.ANY_DURATION * 2), epicInMap.getEndTime());
    }

    @Test
    void tasksShouldBePrioritizedByStartTime() {
        Task task = new Task("Имя", "Описание", DataForTests.ANY_DURATION, LocalDateTime.now());
        Task twoTask = new Task("Имя", "Описание", DataForTests.ANY_DURATION, LocalDateTime.now().plusMinutes(DataForTests.ANY_DURATION));
        taskManager.setNewTask(task);
        taskManager.setNewTask(twoTask);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        Task firstInSet = prioritizedTasks.iterator().next();

        assertEquals(task.getId(), firstInSet.getId());
    }

    @Test
    void shouldNotAllowTasksWithOverlappingTimes() {
        Task task = new Task("Имя", "Описание", DataForTests.ANY_DURATION, LocalDateTime.now());
        Task overlapTask = new Task("Имя", "Описание", DataForTests.ANY_DURATION, LocalDateTime.now().plusMinutes(DataForTests.ANY_DURATION / 2));
        taskManager.setNewTask(task);

        Exception exception = assertThrows(IllegalStateException.class, () -> taskManager.setNewTask(overlapTask));

        String expectedMessage = "Периоды задач не могут пересекаться";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    protected void addFifeRequestsInHistory() {
        for (int i = 0; i < FIFE_TASK_IN_HISTORY; i++) {
            Epic epic = DataForTests.getNewEpic();
            taskManager.setNewEpic(epic);
            taskManager.getEpicById(epic.getId());
        }
    }
}

