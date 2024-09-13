package ru.yandex.javacource.sosnin.schedule.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sosnin.schedule.exceptions.TaskValidationException;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;
import ru.yandex.javacource.sosnin.schedule.tasks.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager manager) {
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
                }
                break;
            case "POST":
                switch (path.length) {
                    case 2:
                        createTask(h);
                        break;
                    case 3:
                        updateTask(h);
                        break;
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
        List<Subtask> tasks = manager.getAllSubtasks();
        String body = gson.toJson(tasks);
        sendJson(h, body);
    }

    private void getTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                sendJson(h, gson.toJson(manager.getSubtask(taskId.get())));
            }
        } catch (NullPointerException e) {
            sendText(h, "Subtask not found", 404);
        }
    }

    private void createTask(HttpExchange h) throws IOException {
        try {
            manager.createSubtask(gson.fromJson(bodyToString(h), Subtask.class));
            sendText(h, "Subtask created", 201);
        } catch (TaskValidationException e) {
            sendText(h, "Subtask start time is crossing with another task", 406);
        }
    }

    private void updateTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                Subtask subtask = gson.fromJson(bodyToString(h), Subtask.class);
                subtask.setId(taskId.get());
                manager.updateSubtask(subtask);
                sendText(h, "Subtask updated", 201);
            }
        } catch (NullPointerException e) {
            sendText(h, "Subtask not found", 404);
        } catch (TaskValidationException e) {
            sendText(h, "Subtask start time is crossing with another task", 406);
        }
    }

    private void deleteTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                manager.deleteSubtask(taskId.get());
                sendText(h, "Subtask deleted", 200);
            }
        } catch (NullPointerException e) {
            sendText(h, "Subtask not found", 404);
        }
    }
}
