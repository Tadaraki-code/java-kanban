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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HTTPTasksServer taskServer = new HTTPTasksServer((InMemoryTaskManager) manager);
    Gson gson = taskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        int id = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Task task1 = gson.fromJson(body, Task.class);

        assertEquals(task.getId(), task1.getId(), "ID не совподают");
        assertEquals(task.getName(), task1.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        int id = manager.addNewTask(task);

        Task task2 = new Task("Test 3", "Testing task 3",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 15, 5, 5));
        int id2 = manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Task[] tasks = gson.fromJson(body, Task[].class);


        assertEquals(2, tasks.length, "Массив содержит больше элемент чем должен");

        assertEquals(id, tasks[0].getId(), "ID первой задачи не совпадает");
        assertEquals(id2, tasks[1].getId(), "ID второй задачи не совпадает");
        assertEquals(task.getName(), tasks[0].getName(), "Имя первой задачи не совпадает");
        assertEquals(task2.getName(), tasks[1].getName(), "Имя второй задачи не совпадает");
    }

    @Test
    public void tesUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        int id = manager.addNewTask(task);

        Task updateTask = new Task("update Test 2", "update task 2", id,
                TaskStatus.DONE, Duration.ofMinutes(5), LocalDateTime.now());

        String taskJson = gson.toJson(updateTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        String body = response2.body();

        Task updateTaskFromManager = gson.fromJson(body, Task.class);


        assertNotNull(updateTaskFromManager, "Задачи не возвращаются");
        assertNotEquals(task.getName(), updateTaskFromManager.getName(), "Именна задач совподают");
        assertNotEquals(task.getDescription(), updateTaskFromManager.getDescription(), "Описание задач совподают");
        assertNotEquals(task.getStatus(), updateTaskFromManager.getStatus(), "Статусы задач совподают");
        assertEquals(task.getId(), updateTaskFromManager.getId(), "ID задач не совподают");
    }

    @Test
    public void tesDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        int id = manager.addNewTask(task);

        assertNotNull(manager.getTask(id));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Task> TasksList = (ArrayList<Task>) manager.getTasksList();

        assertEquals(0, TasksList.size(), "Список содержит больше элемент чем должен");
        assertNull(manager.getTask(id));
    }

    @Test
    public void tesDeleteAllTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        int id = manager.addNewTask(task);

        Task task2 = new Task("Test 3", "Testing task 3",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 15, 5, 5));
        int id2 = manager.addNewTask(task2);

        Task task3 = new Task("Test 3", "Testing task 3",
                TaskStatus.NEW, Duration.ofMinutes(5),
                LocalDateTime.of(2025, 11, 14, 5, 5));
        int id3 = manager.addNewTask(task3);

        assertEquals(3, manager.getTasksList().size(), "Список содержит больше элемент чем должен");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/all");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Task> TasksList = (ArrayList<Task>) manager.getTasksList();

        assertEquals(0, TasksList.size(), "Список содержит больше элемент чем должен");
        assertNull(manager.getTask(id));
        assertNull(manager.getTask(id2));
        assertNull(manager.getTask(id3));
    }
}
