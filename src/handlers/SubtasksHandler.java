package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());


        switch (endpoint) {
            case GET_SUBTASKS: {
                handleGetSubtasks(exchange);
                break;
            }
            case GET_SUBTASK: {
                handleGetSubtask(exchange);
                break;
            }
            case POST_SUBTASK: {
                handlePostSubtask(exchange);
                break;
            }
            case DELETE_SUBTASK: {
                handleDeleteSubtask(exchange);
                break;
            }
            case DELETE_ALL_SUBTASKS: {
                handleDeleteAllSubtask(exchange);
                break;
            }
            default:
                super.sendNotFound(exchange, "Некоректное обращение к методу!");
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        ArrayList<Subtask> subtaskList = (ArrayList<Subtask>) manager.getSubtasksList();
        if (subtaskList.isEmpty()) {
            super.sendText(exchange, "Список подзач пуст!", 200);
        } else {
            super.sendText(exchange, gson.toJson(subtaskList), 200);
        }


    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int subtaskId = Integer.parseInt(pathParts[2]);

        if (manager.getSubtask(subtaskId) == null) {
            super.sendNotFound(exchange, "Такой подзадачи нет!");
        } else {
            String jsonBody = gson.toJson(manager.getSubtask(subtaskId));
            super.sendText(exchange, jsonBody, 200);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        JsonElement jsonElement = JsonParser.parseString(body);

        if (jsonElement.isJsonObject()) {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask.getId() == 0) {
                if ((manager.getEpicsList().stream().anyMatch(epic -> epic.getId() == subtask.getEpicId())
                        && manager.addNewSubtask(subtask) != -1)) {
                    super.sendText(exchange, "Подзадача успешно добалена!", 201);
                } else {
                    super.sendHasInteractions(exchange, "Время подзадачи пересекаеться с уже существующей задачей," +
                            " или пренадлежит несуществующему эпику!");
                }
            } else {
                if (manager.updateSubtask(subtask)) {
                    super.sendText(exchange, "Подзадача успешно обнавлена!", 201);
                } else {
                    super.sendHasInteractions(exchange, "Время подзадачи пересекаеться с уже существующей задачей!");
                }
            }
        } else {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write("Поля подзадчи заполнены не коректно!".getBytes());
            exchange.close();
        }

    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int subtaskId = Integer.parseInt(pathParts[2]);

        if (manager.getSubtask(subtaskId) == null) {
            super.sendNotFound(exchange, "Такой задачи нет!");
        } else {
            manager.removeSubtask(subtaskId);
            super.sendText(exchange, "Задача успешно удалена", 200);
        }

    }

    private void handleDeleteAllSubtask(HttpExchange exchange) throws IOException {
        ArrayList<Subtask> subtaskList = (ArrayList<Subtask>) manager.getSubtasksList();
        if (subtaskList.isEmpty()) {
            super.sendText(exchange, "Список подзач пуст!", 200);
        } else {
            manager.cleanAllSubtask();
            super.sendText(exchange, "Подзадачи успешно удалены!", 200);
        }

    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET": {
                if (pathParts.length == 2) {
                    return Endpoint.GET_SUBTASKS;
                }
                return Endpoint.GET_SUBTASK;

            }
            case "POST": {
                return Endpoint.POST_SUBTASK;

            }
            case "DELETE": {
                if (pathParts.length == 3 || pathParts[2].equals("all")) {
                    return Endpoint.DELETE_ALL_SUBTASKS;
                } else if (pathParts.length == 2 || pathParts[1].equals("subtasks")) {
                    return Endpoint.DELETE_SUBTASK;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }

}
