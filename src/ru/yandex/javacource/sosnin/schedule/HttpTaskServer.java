package ru.yandex.javacource.sosnin.schedule;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.sosnin.schedule.handlers.*;
import ru.yandex.javacource.sosnin.schedule.manager.Managers;
import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefaultTaskManager();
        new BaseHttpHandler(manager);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
        server.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
