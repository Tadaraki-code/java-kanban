package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS: {
                handleGetEpics(exchange);
                break;
            }
            case GET_EPIC: {
                handleGetEpic(exchange);
                break;
            }
            case POST_EPIC: {
                handlePostEpic(exchange);
                break;
            }
            case DELETE_EPIC: {
                handleDeleteEpic(exchange);
                break;
            }
            case DELETE_ALL_EPICS: {
                handleDeleteAllEpics(exchange);
                break;
            }
            default:
                super.sendNotFound(exchange, "Некоректное обращение к методу!");
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        ArrayList<Epic> epicsList = (ArrayList<Epic>) manager.getEpicsList();
        if (epicsList.isEmpty()) {
            super.sendText(exchange, "Список эпиков пуст!", 200);
        } else {
            super.sendText(exchange, gson.toJson(epicsList), 200);
        }


    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int epicId = Integer.parseInt(pathParts[2]);

        if (manager.getEpic(epicId) == null) {
            super.sendNotFound(exchange, "Такого эпика нет!");
        } else {
            String jsonBody = gson.toJson(manager.getEpic(epicId));
            super.sendText(exchange, jsonBody, 200);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        JsonElement jsonElement = JsonParser.parseString(body);

        if (jsonElement.isJsonObject()) {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic.getId() == 0) {
                manager.addNewEpic(epic);
                super.sendText(exchange, "Эпик успешно добален!", 201);
            } else {
                if (manager.updateEpic(epic)) {
                    super.sendText(exchange, "Эпик успешно обнавлен!", 201);
                } else {
                    super.sendNotFound(exchange, "Такого эпика нет!");
                }
            }
        } else {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write("Поля эпика заполнены не коректно!".getBytes());
            exchange.close();
        }

    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int epicId = Integer.parseInt(pathParts[2]);

        if (manager.getEpic(epicId) == null) {
            super.sendNotFound(exchange, "Такого эпика нет!");
        } else {
            manager.removeEpic(epicId);
            super.sendText(exchange, "Эпик успешно удален", 200);
        }

    }

    private void handleDeleteAllEpics(HttpExchange exchange) throws IOException {
        ArrayList<Epic> epicsList = (ArrayList<Epic>) manager.getEpicsList();
        if (epicsList.isEmpty()) {
            super.sendText(exchange, "Список эпиков пуст!", 200);
        } else {
            manager.cleanAllEpics();
            super.sendText(exchange, "Эпики успешно удалены!", 200);
        }

    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET": {
                if (pathParts.length == 2) {
                    return Endpoint.GET_EPICS;
                }
                return Endpoint.GET_EPIC;

            }
            case "POST": {
                return Endpoint.POST_EPIC;

            }
            case "DELETE": {
                if (pathParts.length == 3 || pathParts[2].equals("all")) {
                    return Endpoint.DELETE_ALL_EPICS;
                } else if (pathParts.length == 2 || pathParts[1].equals("epics")) {
                    return Endpoint.DELETE_EPIC;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }

}
