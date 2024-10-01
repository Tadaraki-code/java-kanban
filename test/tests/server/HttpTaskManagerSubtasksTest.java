package tests.server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HTTPTasksServer;
import tasks.Epic;
import tasks.Subtask;
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
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskManagerSubtasksTest {

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
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.DONE, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        List<Subtask> tasksFromManager = manager.getSubtasksList();

        assertNotNull(tasksFromManager, "Подзадача не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество Подзадача");
        assertEquals("Subtask name", tasksFromManager.getFirst().getName(), "Некорректное имя Подзадача");
        assertEquals("Subtask description", tasksFromManager.getFirst().getDescription(),
                "Некорректное описание подзадачи");
        assertEquals(id, tasksFromManager.getFirst().getEpicId(), "Некорректное имя Подзадача");
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.DONE, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));
        int subtaskId = manager.addNewSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Subtask subtask1 = gson.fromJson(body, Subtask.class);

        assertEquals(subtask.getId(), subtask1.getId(), "ID не совподают");
        assertEquals(subtask.getName(), subtask1.getName(), "Некорректное имя подзадач");
        assertEquals(subtask.getDescription(), subtask1.getDescription(), "Некорректное описание подзадач");
        assertEquals(subtask.getEpicId(), subtask1.getEpicId(), "Некорректное ID-эпиков подзадач");
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.DONE, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));
        int subtaskId = manager.addNewSubtask(subtask);


        Subtask subtask2 = new Subtask("Subtask two name", "Subtask two description", TaskStatus.NEW, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 12, 14, 40));
        int subtaskId2 = manager.addNewSubtask(subtask2);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Subtask[] subtasks = gson.fromJson(body, Subtask[].class);


        assertEquals(2, subtasks.length, "Массив содержит больше элемент чем должен");

        assertEquals(subtaskId, subtasks[0].getId(), "ID первой подзадачи не совпадает");
        assertEquals(subtaskId2, subtasks[1].getId(), "ID второй подзадачи не совпадает");
        assertEquals(subtask.getName(), subtasks[0].getName(), "Имя первой подзадачи не совпадает");
        assertEquals(subtask2.getName(), subtasks[1].getName(), "Имя второй подзадачи не совпадает");
        assertEquals(subtask.getEpicId(), subtasks[0].getEpicId(), "ID-эпика первой подзадачи не совпадает");
        assertEquals(subtask2.getEpicId(), subtasks[1].getEpicId(), "ID-эпика  второй подзадачи не совпадает");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.IN_PROGRESS, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));
        int subtaskId = manager.addNewSubtask(subtask);

        Subtask subtaskUpdate = new Subtask("Subtask update name", "Subtask update description",
                subtaskId, TaskStatus.DONE, id, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 11, 11, 14, 40));

        String taskJson = gson.toJson(subtaskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();


        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        String body = response2.body();

        Subtask updateSubtaskFromManager = gson.fromJson(body, Subtask.class);

        ArrayList<Subtask> SubtaskList = (ArrayList<Subtask>) manager.getSubtasksList();
        assertEquals(1, SubtaskList.size(), "Список содержит больше элемент чем должен");


        assertNotNull(updateSubtaskFromManager, "Подзадача не возвращаются");
        assertNotEquals(subtask.getName(), updateSubtaskFromManager.getName(), "Именна подзадач совподают");
        assertNotEquals(subtask.getDescription(), updateSubtaskFromManager.getDescription(), "Описание подзадач совподают");
        assertNotEquals(subtask.getStatus(), updateSubtaskFromManager.getStatus(), "Статусы подзадач не совподают");
        assertEquals(subtask.getEpicId(), updateSubtaskFromManager.getEpicId(), "ID подзадач не совподают");
        assertEquals(subtask.getEpicId(), updateSubtaskFromManager.getEpicId(), "ID-эпиков подзадач не совподают");
    }

    @Test
    public void tesDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.IN_PROGRESS, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));
        int subtaskId = manager.addNewSubtask(subtask);

        assertNotNull(manager.getSubtask(subtaskId));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Subtask> SubtaskList = (ArrayList<Subtask>) manager.getSubtasksList();

        assertEquals(0, SubtaskList.size(), "Список содержит больше элемент чем должен");
        assertNull(manager.getSubtask(subtaskId));
    }

    @Test
    public void tesDeleteAllSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", TaskStatus.IN_PROGRESS, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 11, 14, 40));
        int subtaskId = manager.addNewSubtask(subtask);

        Subtask subtask2 = new Subtask("Subtask two name", "Subtask two description", TaskStatus.IN_PROGRESS, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 12, 14, 40));
        int subtask2Id = manager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Subtask three name", "Subtask three description", TaskStatus.IN_PROGRESS, id
                , Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 13, 14, 40));
        int subtask3Id = manager.addNewSubtask(subtask3);

        assertEquals(3, manager.getSubtasksList().size(), "Список содержит больше элемент чем должен");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/all");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Subtask> SubtaskList = (ArrayList<Subtask>) manager.getSubtasksList();


        assertEquals(0, SubtaskList.size(), "Список содержит больше элементов чем должен");
        assertEquals(0, manager.getEpic(id).getSubtaskList().size(),
                "Список подзадач эпика большен быть пустым");
        assertNull(manager.getSubtask(subtaskId));
        assertNull(manager.getSubtask(subtask2Id));
        assertNull(manager.getSubtask(subtask3Id));
    }
}
