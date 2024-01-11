package managers;

import main.java.ru.yandex.practicum.managers.HistoryManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static managers.TaskManagerTest.TWO_TASKS_IN_LIST;
import static managers.TaskManagerTest.ZERO_TASK_IN_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HistoryManagerTest {
    public static final int FIRST_EPIC_ID = 1;
    private static HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testHistoryAddGetHistoryRemove() {
        Epic epic = new Epic("Имя", "Описание");
        epic.setId(FIRST_EPIC_ID);
        historyManager.add(epic);

        Epic epicTwo = new Epic("Имя", "Описание");
        historyManager.add(epicTwo);

        assertEquals(TWO_TASKS_IN_LIST, historyManager.getHistory().size());
        assertEquals(epic, historyManager.getHistory().get(FIRST_EPIC_ID));
        historyManager.remove(FIRST_EPIC_ID);
        assertFalse(historyManager.getHistory().contains(epic));
    }

    @Test
    void testGetHistoryNoGetReturnSizeZero() {
        assertEquals(ZERO_TASK_IN_LIST, historyManager.getHistory().size());
    }

    @Test
    void testRemoveInFirstTask() {
        Epic epicOne = new Epic("Имя", "Описание");
        epicOne.setId(1);
        historyManager.add(epicOne);

        Epic epicTwo = new Epic("Имя", "Описание");
        epicTwo.setId(2);
        historyManager.add(epicTwo);

        Epic epicTree = new Epic("Имя", "Описание");
        epicTree.setId(3);
        historyManager.add(epicTree);

        historyManager.remove(1);

        assertEquals(TWO_TASKS_IN_LIST, historyManager.getHistory().size());
        assertEquals(epicTwo, historyManager.getHistory().get(1));
        assertEquals(epicTree, historyManager.getHistory().get(0));
        assertFalse(historyManager.getHistory().contains(epicOne));
    }

    @Test
    void testRemoveInMidlTask() {
        Epic epicOne = new Epic("Имя", "Описание");
        epicOne.setId(1);
        historyManager.add(epicOne);

        Epic epicTwo = new Epic("Имя", "Описание");
        epicTwo.setId(2);
        historyManager.add(epicTwo);

        Epic epicTree = new Epic("Имя", "Описание");
        epicTree.setId(3);
        historyManager.add(epicTree);

        historyManager.remove(2);

        assertEquals(TWO_TASKS_IN_LIST, historyManager.getHistory().size());
        assertEquals(epicOne, historyManager.getHistory().get(1));
        assertEquals(epicTree, historyManager.getHistory().get(0));
        assertFalse(historyManager.getHistory().contains(epicTwo));
    }

    @Test
    void testRemoveInLastTask() {
        Epic epicOne = new Epic("Имя", "Описание");
        epicOne.setId(1);
        historyManager.add(epicOne);

        Epic epicTwo = new Epic("Имя", "Описание");
        epicTwo.setId(2);
        historyManager.add(epicTwo);

        Epic epicTree = new Epic("Имя", "Описание");
        epicTree.setId(3);
        historyManager.add(epicTree);

        historyManager.remove(3);

        assertEquals(TWO_TASKS_IN_LIST, historyManager.getHistory().size());
        assertEquals(epicOne, historyManager.getHistory().get(1));
        assertEquals(epicTwo, historyManager.getHistory().get(0));
        assertFalse(historyManager.getHistory().contains(epicTree));
    }
}
