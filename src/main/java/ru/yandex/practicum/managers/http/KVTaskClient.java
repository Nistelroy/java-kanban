package main.java.ru.yandex.practicum.managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private final HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
    private HttpRequest httpRequest;
    private HttpResponse<String> httpResponse;
    private final String URL;
    private final String API_TOKEN;

    public KVTaskClient(String url) {
        this.URL = url;
        this.API_TOKEN = register();
    }

    public String register() {
        httpRequest = requestBuilder
                .uri(URI.create(URL + "/register"))
                .GET()
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }

        return httpResponse.body();
    }

    public String load(String key) {
        httpRequest = requestBuilder
                .uri(URI.create(URL + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                .GET()
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return httpResponse.body();
    }

    public void put(String key, String json) {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        httpRequest = requestBuilder
                .uri(URI.create(URL + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .POST(body)
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }
}