package tests.server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HTTPTasksServer;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerPrioritizedTest {


    TaskManager manager = new InMemoryTaskManager();
    HTTPTasksServer taskServer = new HTTPTasksServer((InMemoryTaskManager) manager);
    Gson gson = taskServer.getGson();


    @BeforeEach
    public void setUp() throws IOException {
        manager.cleanAllTasks();
        manager.cleanAllSubtask();
        manager.cleanAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }


    @Test
    public void testGetPrioritizedList() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 13, 5, 5));
        int id = manager.addNewTask(task);

        Task task2 = new Task("Test 2", "Testing task 3",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 15, 5, 5));
        int id2 = manager.addNewTask(task2);

        Task task3 = new Task("Test 3", "Testing task 3",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 14, 5, 5));
        int id3 = manager.addNewTask(task3);

        Task task4 = new Task("Test 4", "Testing task 4",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 10, 14, 5, 5));
        int id4 = manager.addNewTask(task4);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        String body = response.body();
        Task[] tasks = gson.fromJson(body, Task[].class);
        assertEquals(4, tasks.length, "ID не совподают");


        assertEquals(task4.getId(), tasks[0].getId(), "ID не совподают");
        assertEquals(task4.getName(), tasks[0].getName(), "Имена не совподают");
        assertEquals(task4.getDescription(), tasks[0].getDescription(), "Описание не совподают");

        assertEquals(task.getId(), tasks[1].getId(), "ID не совподают");
        assertEquals(task.getName(), tasks[1].getName(), "Имена не совподают");
        assertEquals(task.getDescription(), tasks[1].getDescription(), "Описание не совподают");

        assertEquals(task3.getId(), tasks[2].getId(), "ID не совподают");
        assertEquals(task3.getName(), tasks[2].getName(), "Имена не совподают");
        assertEquals(task3.getDescription(), tasks[2].getDescription(), "Описание не совподают");

        assertEquals(task2.getId(), tasks[3].getId(), "ID не совподают");
        assertEquals(task2.getName(), tasks[3].getName(), "Имена не совподают");
        assertEquals(task2.getDescription(), tasks[3].getDescription(), "Описание не совподают");
    }
}
