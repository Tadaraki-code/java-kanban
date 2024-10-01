package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                handleGetTasks(exchange);
                break;
            }
            case GET_TASK: {
                handleGetTask(exchange);
                break;
            }
            case POST_TASK: {
                handlePostTask(exchange);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(exchange);
                break;
            }
            case DELETE_ALL_TASKS: {
                handleDeleteAllTasks(exchange);
                break;
            }
            default:
                super.sendNotFound(exchange, "Некоректное обращение к методу!");
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        ArrayList<Task> tasksList = (ArrayList<Task>) manager.getTasksList();
        if (tasksList.isEmpty()) {
            super.sendText(exchange, "Список задач пуст!", 200);
        } else {
            super.sendText(exchange, gson.toJson(tasksList), 200);
        }
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int taskId = Integer.parseInt(pathParts[2]);

        if (manager.getTask(taskId) == null) {
            super.sendNotFound(exchange, "Такой задачи нет!");
        } else {
            String jsonBody = gson.toJson(manager.getTask(taskId));
            super.sendText(exchange, jsonBody, 200);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        JsonElement jsonElement = JsonParser.parseString(body);

        if (jsonElement.isJsonObject()) {
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() == 0) {
                if (manager.addNewTask(task) == -1) {
                    super.sendHasInteractions(exchange, "Время задачи пересекаеться с уже существующей задачей!");
                } else {
                    super.sendText(exchange, "Задача успешно добалена!", 201);
                }
            } else {
                if (manager.updateTask(task)) {
                    super.sendText(exchange, "Задача успешно обнавлена!", 201);
                } else {
                    super.sendHasInteractions(exchange, "Время задачи пересекаеться с уже существующей задачей!");
                }
            }
        } else {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write("Поля задачи заполнены не коректно!".getBytes());
            exchange.close();
        }

    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int taskId = Integer.parseInt(pathParts[2]);

        if (manager.getTask(taskId) == null) {
            super.sendNotFound(exchange, "Такой задачи нет!");
        } else {
            manager.removeTask(taskId);
            super.sendText(exchange, "Задача успешно удалена", 200);
        }

    }

    private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
        ArrayList<Task> tasksList = (ArrayList<Task>) manager.getTasksList();
        if (tasksList.isEmpty()) {
            super.sendText(exchange, "Список задач пуст!", 200);
        } else {
            manager.cleanAllTasks();
            super.sendText(exchange, "задачи успешно удалены!", 200);
        }

    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET": {
                if (pathParts.length == 2) {
                    return Endpoint.GET_TASKS;
                }
                return Endpoint.GET_TASK;

            }
            case "POST": {
                return Endpoint.POST_TASK;

            }
            case "DELETE": {
                if (pathParts.length == 3 || pathParts[2].equals("all")) {
                    return Endpoint.DELETE_ALL_TASKS;
                } else if (pathParts.length == 2 || pathParts[1].equals("tasks")) {
                    return Endpoint.DELETE_TASK;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }

}
