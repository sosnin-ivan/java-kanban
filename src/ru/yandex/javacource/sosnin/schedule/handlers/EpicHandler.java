package ru.yandex.javacource.sosnin.schedule.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sosnin.schedule.exceptions.TaskValidationException;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;
import ru.yandex.javacource.sosnin.schedule.tasks.Epic;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String[] path = h.getRequestURI().getPath().split("/");
        switch (h.getRequestMethod()) {
            case "GET":
                switch (path.length) {
                    case 2:
                        getAllTasks(h);
                        break;
                    case 3:
                        getTask(h);
                        break;
                    case 4:
                        if (path[3].equals("subtasks")) {
                            getSubtasks(h);
                        }
                        break;
                }
                break;
            case "POST":
                if (path.length == 2) {
                    createTask(h);
                }
                break;
            case "DELETE":
                if (path.length == 3) {
                    deleteTask(h);
                }
            default:
                sendText(h, "Page not found", 404);
        }
    }

    private void getAllTasks(HttpExchange h) throws IOException {
        List<Epic> epics = manager.getAllEpics();
        String body = gson.toJson(epics);
        sendJson(h, body);
    }

    private void getTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                sendJson(h, gson.toJson(manager.getEpic(taskId.get())));
            }
        } catch (NullPointerException e) {
            sendText(h, "Epic not found", 404);
        }
    }

    private void getSubtasks(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                sendJson(h, gson.toJson(manager.getSubtasksByEpic(taskId.get())));
            }
        } catch (NullPointerException e) {
            sendText(h, "Epic not found", 404);
        }
    }

    private void createTask(HttpExchange h) throws IOException {
        try {
            manager.createEpic(gson.fromJson(bodyToString(h), Epic.class));
            sendText(h, "Epic created", 201);
        } catch (TaskValidationException e) {
            sendText(h, "Epic start time is crossing with another task", 406);
        }
    }

    private void deleteTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                manager.deleteEpic(taskId.get());
                sendText(h, "Epic deleted", 200);
            }
        } catch (NullPointerException e) {
            sendText(h, "Epic not found", 404);
        }
    }
}
