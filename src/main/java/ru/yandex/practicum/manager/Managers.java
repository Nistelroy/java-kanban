package main.java.ru.yandex.practicum.manager;

public class Managers {

   public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
