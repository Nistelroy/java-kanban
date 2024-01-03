package test.main.java.ru.yandex.practicum.managers;

import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final Path file = Paths.get("src/main/resources/test_data.csv");
    @Override
    protected FileBackedTasksManager createTaskManager() {

        return new FileBackedTasksManager(file);
    }
}