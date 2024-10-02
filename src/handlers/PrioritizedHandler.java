package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    Gson gson;

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_PRIORITIZED: {
                handleGetPrioritized(exchange);
                break;
            }
            default:
                super.sendNotFound(exchange, "Некоректное обращение к методу!");
        }
    }

    public void handleGetPrioritized(HttpExchange exchange) throws IOException {
        TreeSet<Task> prioritizedSet = manager.getPrioritizedTask();
        if (prioritizedSet.isEmpty()) {
            super.sendText(exchange, "Список задач пуст!", 200);
        } else {
            super.sendText(exchange, gson.toJson(prioritizedSet), 200);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET": {
                return Endpoint.GET_PRIORITIZED;
            }
        }
        return Endpoint.UNKNOWN;
    }

}
