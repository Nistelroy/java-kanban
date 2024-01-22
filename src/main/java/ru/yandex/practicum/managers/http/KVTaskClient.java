package main.java.ru.yandex.practicum.managers.http;

import main.java.ru.yandex.practicum.managers.exception.HttpResponseException;

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
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = register();
    }

    public String register() {
        httpRequest = requestBuilder
                .uri(URI.create(url + "/register"))
                .GET()
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
            if (httpResponse.statusCode() != 200) {
                throw new HttpResponseException("Сервер вернул не код 200");
            }
        } catch (IOException | InterruptedException e) {
            throw new HttpResponseException("Ошибка регистрации");
        }

        return httpResponse.body();
    }

    public String load(String key) {
        httpRequest = requestBuilder
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new HttpResponseException("Ошибка выгрузки");
        }

        return httpResponse.body();
    }

    public void put(String key, String json) {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        httpRequest = requestBuilder
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .POST(body)
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, stringBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new HttpResponseException("Ошибка загрузки");
        }
    }
}