package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTasksServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private HttpServer httpServer;
    private final Gson gson;

    public HTTPTasksServer(FileBackedTaskManager manager) {
        this.manager = manager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
    }

    public HTTPTasksServer(InMemoryTaskManager manager) {
        this.manager = manager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
    }

    public static void main(String[] args) {
        FileBackedTaskManager manager = Managers.getDefault();
        HTTPTasksServer server = new HTTPTasksServer(manager);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace(); // Вывод ошибки
        }
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager, gson));
        httpServer.createContext("/epics", new EpicsHandler(manager, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager, gson));
        httpServer.createContext("/history", new HistoryHandler(manager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager, gson));
        httpServer.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("Сервер остановлен.");
        }
    }

    public Gson getGson() {
        return gson;
    }
}