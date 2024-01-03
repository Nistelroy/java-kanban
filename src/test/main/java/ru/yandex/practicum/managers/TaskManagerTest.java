package test.main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void testAddTask() {
        Task task = new Task("Имя", "Описание");
        taskManager.setNewTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()));
    }


}