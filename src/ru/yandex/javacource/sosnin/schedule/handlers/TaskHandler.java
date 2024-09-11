package ru.yandex.javacource.sosnin.schedule.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.sosnin.schedule.exceptions.TaskValidationException;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;
import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager manager) {
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
        List<Task> tasks = manager.getAllTasks();
        String body = gson.toJson(tasks);
        sendJson(h, body);
    }

    private void getTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                sendJson(h, gson.toJson(manager.getTask(taskId.get())));
            }
        } catch (NullPointerException e) {
            sendText(h, "Task not found", 404);
        }
    }

    private void createTask(HttpExchange h) throws IOException {
        try {
            manager.createTask(gson.fromJson(bodyToString(h), Task.class));
            sendText(h, "Task created", 201);
        } catch (TaskValidationException e) {
            sendText(h, "Task start time is crossing with another task", 406);
        }
    }

    private void updateTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                Task task = gson.fromJson(bodyToString(h), Task.class);
                task.setId(taskId.get());
                manager.updateTask(task);
                sendText(h, "Task updated", 201);
            }
        } catch (NullPointerException e) {
            sendText(h, "Task not found", 404);
        } catch (TaskValidationException e) {
            sendText(h, "Task start time is crossing with another task", 406);
        }
    }

    private void deleteTask(HttpExchange h) throws IOException {
        try {
            Optional<Integer> taskId = getTaskId(h);
            if (taskId.isPresent()) {
                manager.deleteTask(taskId.get());
                sendText(h, "Task deleted", 200);
            }
        } catch (NullPointerException e) {
            sendText(h, "Task not found", 404);
        }
    }
}
