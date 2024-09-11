package ru.yandex.javacource.sosnin.schedule.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.sosnin.schedule.typeAdapters.*;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class BaseHttpHandler implements HttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager manager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .serializeNulls()
                .create();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
    }

    protected void sendText(HttpExchange h, String response, int code) throws IOException {
        try (OutputStream os = h.getResponseBody()) {
            h.sendResponseHeaders(code, response.length());
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
        h.close();
    }

    protected void sendJson(HttpExchange h, String response) throws IOException {
        try (OutputStream os = h.getResponseBody()) {
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(200, response.length());
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
        h.close();
    }

    protected Optional<Integer> getTaskId(HttpExchange h) {
        String[] pathParts = h.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected String bodyToString(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }
}
