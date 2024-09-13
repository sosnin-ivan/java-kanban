package ru.yandex.javacource.sosnin.schedule.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;
import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String[] path = h.getRequestURI().getPath().split("/");
        if (h.getRequestMethod().equals("GET") && path.length == 2) {
            getHistory(h);
        } else {
            sendText(h, "Page not found", 404);
        }
    }

    private void getHistory(HttpExchange h) throws IOException {
        List<Task> history = manager.getHistory();
        String body = gson.toJson(history);
        sendJson(h, body);
    }
}
