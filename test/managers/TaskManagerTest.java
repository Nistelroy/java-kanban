package managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    public static final Path file = Paths.get("src/main/resources/test_data.csv");
    public static final int INCORRECT_ID = 100;
    public static final int ZERO_TASK_IN_LIST = 0;
    public static final int ONE_TASK_IN_LIST = 1;
    public static final int TWO_TASKS_IN_LIST = 2;
    public static final int ANY_DURATION = 30;
    protected T taskManager;
    private Task testTask;
    private Epic testEpic;
    private Subtask testSubtask;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
        getTasksForTests();
    }

    @Test
    void testAddTaskCorrectTaskInMap() {
        taskManager.setNewTask(testTask);

        assertEquals(testTask, taskManager.getTaskById(testTask.getId()));
    }

    @Test
    void testGetTaskIncorrectIdReturnNull() {
        assertNull(taskManager.getTaskById(INCORRECT_ID));
    }

    @Test
    void testAddEpicCorrectEpicInMap() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);

        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testGetEpicIncorrectIdReturnNull() {
        assertNull(taskManager.getEpicById(INCORRECT_ID));
    }

    @Test
    void testAddSubtaskCorrectSubtaskInMap() {
        taskManager.setNewSubtask(testSubtask);

        assertEquals(testSubtask, taskManager.getSubtaskById(testSubtask.getId()));
    }

    @Test
    void testAddSubtaskCorrectIdSubtaskInEpic() {
        taskManager.setNewSubtask(testSubtask);

        assertEquals(testEpic.getIdSubtask().get(0), testSubtask.getId());
    }

    @Test
    void testAddSubtaskCorrectIdEpicInSubtask() {
        taskManager.setNewSubtask(testSubtask);

        assertEquals(testEpic.getId(), taskManager.getSubtaskById(testSubtask.getId()).getIdEpic());
    }

    @Test
    void testGetSubtaskIncorrectIdReturnNull() {
        assertNull(taskManager.getSubtaskById(INCORRECT_ID));
    }

    @Test
    void testGetHistoryNoGetReturnSizeZero() {
        taskManager.setNewTask(testTask);

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testGetHistoryOneGetReturnSizeOne() {
        taskManager.setNewTask(testTask);
        taskManager.getTaskById(testTask.getId());

        assertEquals(ONE_TASK_IN_LIST, taskManager.getHistory().size());
    }

    @Test
    void testUpdateTaskCorrectTaskInMap() {
        taskManager.setNewTask(testTask);

        Task newTask = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION));
        newTask.setId(testTask.getId());
        taskManager.updateTaskInMap(newTask);

        assertEquals(newTask, taskManager.getTaskById(testTask.getId()));
    }

    @Test
    void testUpdateEpicCorrectEpicInMap() {
        Epic newEpic = new Epic("Имя", "Описание");
        newEpic.setId(testEpic.getId());
        taskManager.updateEpicInMap(newEpic);

        assertEquals(newEpic, taskManager.getEpicById(testEpic.getId()));
    }

    @Test
    void testUpdateSubtaskCorrectSubtaskInMap() {
        taskManager.setNewSubtask(testSubtask);

        Subtask newSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        newSubtask.setId(testSubtask.getId());
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(newSubtask, taskManager.getSubtaskById(testSubtask.getId()));
    }

    @Test
    void testUpdateSubtaskStatusDoneInEpicStatusDone() {
        taskManager.setNewSubtask(testSubtask);

        Subtask newSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        newSubtask.setId(testSubtask.getId());
        newSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtaskInMap(newSubtask);

        assertEquals(TaskStatus.DONE, testEpic.getStatus());
    }

    @Test
    void testDeleteTaskInMapAbsent() {
        taskManager.setNewTask(testTask);
        taskManager.deleteTaskById(testTask.getId());

        assertNull(taskManager.getTaskById(testTask.getId()));
    }

    @Test
    void testDeleteEpicInMapAbsent() {
        taskManager.deleteEpicById(testEpic.getId());

        assertNull(taskManager.getEpicById(testEpic.getId()));
    }

    @Test
    void testDeleteEpicInMapSubtaskAlsoDelete() {
        taskManager.setNewSubtask(testSubtask);
        taskManager.deleteEpicById(testEpic.getId());

        assertNull(taskManager.getSubtaskById(testSubtask.getId()));
    }

    @Test
    void testDeleteSubtaskInMapAbsent() {
        taskManager.setNewSubtask(testSubtask);
        taskManager.deleteSubtaskById(testSubtask.getId());

        assertNull(taskManager.getSubtaskById(testSubtask.getId()));
    }

    @Test
    void testDeleteSubtaskIdInEpicAlsoDelete() {
        taskManager.setNewSubtask(testSubtask);
        taskManager.deleteSubtaskById(testSubtask.getId());

        assertFalse(testEpic.getIdSubtask().contains(testSubtask.getId()));
    }

    @Test
    void testDeleteSubtaskStatusDoneEpicStatusNew() {
        taskManager.setNewSubtask(testSubtask);
        testSubtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtaskInMap(testSubtask);

        taskManager.deleteSubtaskById(testSubtask.getId());

        assertEquals(TaskStatus.NEW, testEpic.getStatus());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdAddTaskReturnList() {
        taskManager.setNewSubtask(testSubtask);

        assertEquals(ONE_TASK_IN_LIST, testEpic.getIdSubtask().size());
    }

    @Test
    void testGetAllSubtaskFromEpicByIdReturnClearList() {
        assertEquals(ZERO_TASK_IN_LIST, testEpic.getIdSubtask().size());
    }

    @Test
    void testGetAllTaskAddTaskReturnListContTask() {
        taskManager.setNewTask(testTask);

        assertTrue(taskManager.getAllTask().contains(testTask));
    }

    @Test
    void testGetAllTaskReturnListNoContTask() {
        assertFalse(taskManager.getAllTask().contains(testTask));
    }

    @Test
    void testGetAllEpicAddEpicReturnListContEpic() {
        assertTrue(taskManager.getAllEpic().contains(testEpic));
    }

    @Test
    void testGetAllEpicReturnListNoContEpic() {
        taskManager.deleteEpicById(testEpic.getId());
        assertFalse(taskManager.getAllEpic().contains(testEpic));
    }

    @Test
    void testGetAllSubtaskAddSubtaskReturnListContSubtask() {
        taskManager.setNewSubtask(testSubtask);

        assertTrue(taskManager.getAllSubtask().contains(testSubtask));
    }

    @Test
    void testGetAllSubtaskReturnListNoContSubtask() {
        assertFalse(taskManager.getAllSubtask().contains(testSubtask));
    }

    @Test
    void testDeleteAllSubtaskInMapAbsent() {
        taskManager.setNewSubtask(testSubtask);
        Subtask subtaskTwo = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION), testEpic.getId());
        taskManager.setNewSubtask(subtaskTwo);

        taskManager.deleteAllSubtask();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllSubtask().size());
    }

    @Test
    void testDeleteAllTaskInMapAbsent() {
        taskManager.setNewTask(testTask);
        Task testTaskTwo = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION));
        taskManager.setNewTask(testTaskTwo);

        taskManager.deleteAllTask();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllTask().size());
    }

    @Test
    void testDeleteAllEpicInMapAbsent() {
        Epic testEpicTwo = new Epic("Имя", "Описание");
        taskManager.setNewEpic(testEpicTwo);

        taskManager.deleteAllEpic();

        assertEquals(ZERO_TASK_IN_LIST, taskManager.getAllEpic().size());
    }

    @Test
    void getStartTimeTaskReturnStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Имя", "Описание", ANY_DURATION, startTime);
        taskManager.setNewTask(task);
        Task taskInMap = taskManager.getTaskById(task.getId());
        assertEquals(startTime, taskInMap.getStartTime());
    }

    @Test
    void getEndTimeTaskReturnEndTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Имя", "Описание", ANY_DURATION, startTime);
        LocalDateTime endTime = startTime.plusMinutes(ANY_DURATION);
        taskManager.setNewTask(task);
        Task taskInMap = taskManager.getTaskById(task.getId());
        assertEquals(endTime, taskInMap.getEndTime());
    }

    @Test
    void getEndTaskTimeShouldBeNullWithoutStartTime() {
        Task taskWithoutStart = new Task("Имя", "Описание", ANY_DURATION, null);
        taskManager.setNewTask(taskWithoutStart);
        Task taskInMap = taskManager.getTaskById(taskWithoutStart.getId());
        assertNull(taskInMap.getEndTime());
    }

    @Test
    void getEpicStartTimeWithoutSubtasks() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Epic epicInMap = taskManager.getEpicById(epic.getId());
        assertNull(epicInMap.getStartTime());
    }


    @Test
    void getEpicEndTimeWithoutSubtasks() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Epic epicInMap = taskManager.getEpicById(epic.getId());
        assertNull(epicInMap.getStartTime());
    }

    @Test
    void getSubtaskStartTimeShouldBeNullWithoutStartTime() {
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", ANY_DURATION, null, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtaskInMap = taskManager.getSubtaskById(subtask.getId());

        assertNull(subtaskInMap.getEndTime());
        assertNull(epic.getEndTime());
    }

    @Test
    void getSubtaskStartTimeReturnStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", ANY_DURATION, startTime, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtaskInMap = taskManager.getSubtaskById(subtask.getId());

        assertEquals(startTime, subtaskInMap.getStartTime());
        assertEquals(startTime, epic.getStartTime());
        assertEquals(startTime.plusMinutes(ANY_DURATION), subtaskInMap.getEndTime());
        assertEquals(startTime.plusMinutes(ANY_DURATION), epic.getEndTime());
    }


    @Test
    void getEpicEndTimeTwoSubtasksSummDuration() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(epic);
        Subtask subtask = new Subtask("Имя", "Описание", ANY_DURATION, startTime, epic.getId());
        taskManager.setNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Имя", "Описание", ANY_DURATION, startTime.plusMinutes(ANY_DURATION), epic.getId());
        taskManager.setNewSubtask(subtask2);

        Epic epicInMap = taskManager.getEpicById(epic.getId());

        assertEquals(startTime.plusMinutes(ANY_DURATION * 2), epicInMap.getEndTime());
    }

    @Test
    void tasksShouldBePrioritizedByStartTime() {
        Task twoTask = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION));
        taskManager.setNewTask(testTask);
        taskManager.setNewTask(twoTask);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        Task firstInSet = prioritizedTasks.iterator().next();

        assertEquals(testTask.getId(), firstInSet.getId());
    }

    @Test
    void shouldNotAllowTasksWithOverlappingTimes() {
        Task overlapTask = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now().plusMinutes(ANY_DURATION / 2));
        taskManager.setNewTask(testTask);

        Exception exception = assertThrows(IllegalStateException.class, () -> taskManager.setNewTask(overlapTask));

        String expectedMessage = "Периоды задач не могут пересекаться";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private void getTasksForTests() {
        testTask = new Task("Имя", "Описание", ANY_DURATION, LocalDateTime.now());
        testEpic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(testEpic);
        testSubtask = new Subtask("Имя", "Описание", ANY_DURATION, LocalDateTime.now(), testEpic.getId());
    }
}

