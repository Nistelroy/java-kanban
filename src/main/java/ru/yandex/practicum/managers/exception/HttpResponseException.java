package main.java.ru.yandex.practicum.managers.exception;

public class HttpResponseException extends RuntimeException {
    public HttpResponseException(String message) {
        super(message);
    }
}
