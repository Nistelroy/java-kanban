package main;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.http.HttpTaskServer;
import main.java.ru.yandex.practicum.managers.http.KVServer;
import main.java.ru.yandex.practicum.tasks.Epic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final Path file = Paths.get("src/main/resources/data.csv");

    public static void main(String[] args) throws IOException, InterruptedException {
//        KVServer kvServer = new KVServer();
//        kvServer.start();
//        TaskManager taskManager = Managers.getDefault();
//        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
//        httpTaskServer.start();
//        taskManager.setNewTask(new Task("Имя", "Описание", 30, LocalDateTime.now()));


        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
       Epic testEpic = new Epic("Имя", "Описание");
        taskManager.setNewEpic(testEpic);




//        TaskManager taskManager = Managers.getDefault("http://localhost:8078");
//
//        HttpClient client = HttpClient.newHttpClient();
//        URI url = URI.create("http://localhost:8080/tasks/task/");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(url)
//                .GET()
//                .build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
}
