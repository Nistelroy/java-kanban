package main.java.ru.yandex.practicum.managers.disc;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}