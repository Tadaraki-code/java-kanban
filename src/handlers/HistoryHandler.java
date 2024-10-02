package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    Gson gson;

    public HistoryHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_HISTORY: {
                handleGetHistory(exchange);
                break;
            }
            default:
                super.sendNotFound(exchange, "Некоректное обращение к методу!");
        }
    }

    public void handleGetHistory(HttpExchange exchange) throws IOException {
        ArrayList<Task> historyList = (ArrayList<Task>) manager.getHistory();
        if (historyList.isEmpty()) {
            super.sendText(exchange, "Список задач пуст!", 200);
        } else {
            super.sendText(exchange, gson.toJson(historyList), 200);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET": {
                return Endpoint.GET_HISTORY;
            }
        }
        return Endpoint.UNKNOWN;
    }

}
