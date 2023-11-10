package test.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;

import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;
import main.java.ru.yandex.practicum.tasks.TaskStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;


public class InMemoryTaskManagerTest {
    public static final int SEVEN_TASKS_IN_HISTORY = 7;
    public static final int MAX_HISTORY_SIZE = 10;
    public static final int TWO_SUBTASK_IN_TEST_EPIC = 2;
    public static final int TWO_TASK_IN_TEST_MAP = 2;
    public static final int TWO_EPIC_IN_TEST_MAP = 2;
    public static final int TREE_SUBTASK_IN_TEST_MAP = 3;
    public static final int EMPTY = 0;
    private TaskManager taskManager;

    @Before
    public void setUp() {
        try {
            taskManager = Managers.getInMemoryHistoryManagerForTest();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    @Test
    public void getHistorySevenRequestsSevenTaskInHistory() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(7);

        assertEquals("Запросы не сохранились", taskManager.getHistory().size(), SEVEN_TASKS_IN_HISTORY);
    }

    @Test
    public void getHistoryTwelveRequestsTenTaskInHistory() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(7);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);

        assertEquals("Список содержит не 10 тасок", MAX_HISTORY_SIZE, taskManager.getHistory().size());
    }

    @Test
    public void getHistoryNullRequestHistoryEmpty() {
        taskManager.getTaskById(11);
        taskManager.getSubtaskById(22);
        taskManager.getEpicById(8);
        taskManager.getSubtaskById(1);
        taskManager.getSubtaskById(2);

        assertEquals("Список содержит не 10 тасок", EMPTY, taskManager.getHistory().size());
    }

    @Test
    public void getTaskByIdRequestLivingIdTaskMatch() {
        Task task = getNewTestTask();

        assertEquals("Таски в мапе отличается от эталона", task, taskManager.getTaskById(1));
    }

    @Test
    public void getTaskByIdRequestWrongIdTaskNotMatch() {
        Task task = getNewTestTask();

        assertNotEquals("Таски в мапе отличается от эталона", task, taskManager.getTaskById(2));
    }

    @Test
    public void getEpicByIdRequestLivingIdTaskMatch() {
        Epic epic = getNewTestEpic();

        assertEquals("Эпик в мапе отличается от эталона", epic, taskManager.getEpicById(3));
    }

    @Test
    public void getEpicByIdRequestWrongIdTaskNotMatch() {
        Epic epic = getNewTestEpic();

        assertNotEquals("Таски в мапе отличается от эталона", epic, taskManager.getEpicById(6));
    }

    @Test
    public void getSubtaskByIdRequestLivingIdTaskMatch() {
        Subtask subtask = getNewTestSubtask();

        assertEquals("Сабтаска в мапе отличается от эталона", subtask, taskManager.getSubtaskById(4));
    }

    @Test
    public void getSubtaskByIdRequestWrongIdTaskNotMatch() {
        Subtask subtask = getNewTestSubtask();

        assertNotEquals("Таски в мапе отличается от эталона", subtask, taskManager.getSubtaskById(6));
    }

    @Test
    public void setNewTaskCorrectTaskInMap() {
        Task task = new Task("Пробная", "Пробная-пробная");
        taskManager.setNewTask(task);

        assertEquals("Новая таска не сохранилась", task, taskManager.getTaskById(1));
    }

    @Test
    public void setNewEpicCorrectEpicInMap() {
        Epic epic = new Epic("Пробная", "Пробная-пробная");
        taskManager.setNewEpic(epic);

        assertEquals("новый эпик не сохранился", epic, taskManager.getEpicById(1));
    }

    @Test
    public void setNewSubtaskCorrectSubtaskInMap() {
        Subtask subtask = new Subtask("Пробная", "Пробная-пробная", 3);
        taskManager.setNewSubtask(subtask);

        assertEquals("Новая сабтаска не сохранилась", subtask, taskManager.getSubtaskById(1));
    }

    @Test
    public void updateTaskInMapCorrectTaskInMap() {
        Task task = getNewTestTask();
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTaskInMap(task);

        assertEquals("статус существующей таски не обновился",
                task.getStatus(), taskManager.getTaskById(1).getStatus());

    }

    @Test
    public void updateEpicInMapCorrectEpicInMap() {
        Epic epic = getNewTestEpic();
        epic.setName("пробный");
        taskManager.updateEpicInMap(epic);

        assertEquals("имя существующего эпика не обновилось",
                epic.getName(), taskManager.getEpicById(3).getName());
    }

    @Test
    public void updateSubtaskInMapCorrectSubtaskInMap() {
        Subtask subtask = getNewTestSubtask();
        subtask.setDescription("пробный");
        taskManager.updateSubtaskInMap(subtask);

        assertEquals("описание существующей субтаски не обновилось",
                subtask.getDescription(), taskManager.getSubtaskById(4).getDescription());
    }

    @Test
    public void deleteTaskByIdCorrectIdNull() {
        taskManager.deleteTaskById(1);
        assertNull("таска не удалилась", taskManager.getTaskById(1));
    }

    @Test
    public void deleteEpicByIdCorrectIdNull() {
        taskManager.deleteEpicById(3);
        assertNull("эпик не удалился", taskManager.getEpicById(3));
    }

    @Test
    public void deleteEpicByIdCorrectIdSubtaskByEpicNull() {
        taskManager.deleteEpicById(3);
        assertNull("сабтаска не удалилась после удаления эпика", taskManager.getSubtaskById(4));
    }

    @Test
    public void deleteSubtaskByIdCorrectIdNull() {
        taskManager.deleteSubtaskById(4);
        assertNull("сабтаска не удалилась после удаления эпика", taskManager.getSubtaskById(4));
    }

    @Test
    public void deleteSubtaskByIdCorrectIdEpicIdSubtaskNull() {
        int IdEpic = taskManager.getSubtaskById(4).getIdEpic();
        taskManager.deleteSubtaskById(4);
        assertThat("Id собтаски остался в эпике после удаления сабтаски",
                (!(taskManager.getEpicById(3).getIdSubtask().contains(IdEpic))));
    }

    @Test
    public void getAllSubtaskFromEpicByIdCorrectIdSizeTwo() {
        assertEquals("Сабтаски не вписаны в эпик",
                TWO_SUBTASK_IN_TEST_EPIC, taskManager.getAllSubtaskFromEpicById(3).size());
    }

    @Test
    public void getAllTaskCorrectSizeTwo() {
        assertEquals("Вернулось неправильное колличество тасок",
                TWO_TASK_IN_TEST_MAP, taskManager.getAllTask().size());
    }

    @Test
    public void getAllEpicCorrectSizeTwo() {
        assertEquals("Вернулось неправильное колличество эпиков",
                TWO_EPIC_IN_TEST_MAP, taskManager.getAllEpic().size());
    }

    @Test
    public void getAllSubtaskCorrectSizeTree() {
        assertEquals("Вернулось неправильное колличество сабтасок",
                TREE_SUBTASK_IN_TEST_MAP, taskManager.getAllSubtask().size());
    }

    @Test
    public void deleteAllTaskCorrectSizeZero() {
        taskManager.deleteAllTask();

        assertEquals("Таски не удалились", EMPTY, taskManager.getAllTask().size());
    }

    @Test
    public void deleteAllEpicCorrectSizeZero() {
        taskManager.deleteAllEpic();

        assertEquals("Эпики не удалились", EMPTY, taskManager.getAllEpic().size());
    }

    @Test
    public void deleteAllEpicCorrectSizeSubtaskAfterDeleteEpicZero() {
        taskManager.deleteAllEpic();

        assertEquals("после удаления эпиков сабтаски не удалились", EMPTY, taskManager.getAllSubtask().size());
    }

    @Test
    public void deleteAllSubtaskCorrectSizeZero() {
        taskManager.deleteAllSubtask();

        assertEquals("Сабтаски не удалились", EMPTY, taskManager.getAllSubtask().size());
    }

    @Test
    public void deleteAllSubtaskCorrectSizeIdSubtaskInEpicZero() {
        taskManager.deleteAllSubtask();

        assertEquals("в эпиках остались id сабтасок после очистки от сабтасок",
                0, taskManager.getEpicById(3).getIdSubtask().size());
    }

    private static Task getNewTestTask() {
        return new Task("купить", "купить-купить", 1, TaskStatus.NEW);
    }

    private static Epic getNewTestEpic() {
        ArrayList<Integer> subtaskId = new ArrayList<>();
        subtaskId.add(4);
        subtaskId.add(5);
        return new Epic("Поездка", "Поездка-Поездка", 3, TaskStatus.NEW, subtaskId);
    }

    private static Subtask getNewTestSubtask() {
        return new Subtask("машина", "машина-машина", 4, TaskStatus.NEW, 3);
    }
}