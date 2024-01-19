package main.java.ru.yandex.practicum.managers.http;

import com.sun.net.httpserver.HttpServer;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.http.handler.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}
