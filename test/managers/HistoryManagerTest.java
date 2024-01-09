package managers;

import main.java.ru.yandex.practicum.managers.HistoryManager;
import main.java.ru.yandex.practicum.managers.memory.InMemoryHistoryManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HistoryManagerTest {
    public static final int FIRST_EPIC_ID = 1;
    private static HistoryManager historyManager;

    @BeforeAll
    static void beforeAll() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testHistoryAddGetHistoryRemove() {
        Epic epic = DataForTests.getNewEpic();
        epic.setId(FIRST_EPIC_ID);
        historyManager.add(epic);

        Epic epicTwo = DataForTests.getNewEpic();
        historyManager.add(epicTwo);

        assertEquals(DataForTests.TWO_TASKS_IN_LIST, historyManager.getHistory().size());
        assertEquals(epic, historyManager.getHistory().get(FIRST_EPIC_ID));
        historyManager.remove(FIRST_EPIC_ID);
        assertFalse(historyManager.getHistory().contains(epic));
    }

    @Test
    void testGetHistoryNoGetReturnSizeZero() {
        assertEquals(DataForTests.ZERO_TASK_IN_LIST, historyManager.getHistory().size());
    }
}
