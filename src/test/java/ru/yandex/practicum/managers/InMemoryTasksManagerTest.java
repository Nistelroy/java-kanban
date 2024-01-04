package test.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.memory.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}