package tests.server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HTTPTasksServer;
import tasks.Epic;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerEpicsTest {

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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        List<Epic> tasksFromManager = manager.getEpicsList();

        assertNotNull(tasksFromManager, "эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпики");
        assertEquals("Epic name", tasksFromManager.getFirst().getName(), "Некорректное имя эпики");
        assertEquals("Epic description", tasksFromManager.getFirst().getDescription(),
                "Некорректное описание эпики");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        ;
        int id = manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Epic epic1 = gson.fromJson(body, Epic.class);

        assertEquals(epic.getId(), epic1.getId(), "ID не совподают");
        assertEquals(epic.getName(), epic1.getName(), "Некорректное имя эпика");
        assertEquals(epic.getDescription(), epic1.getDescription(), "Некорректное описание эпика");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        ;
        int id = manager.addNewEpic(epic);


        Epic epic2 = new Epic("Epic two name", "Epic two description");
        ;
        int id2 = manager.addNewEpic(epic2);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Epic[] epics = gson.fromJson(body, Epic[].class);


        assertEquals(2, epics.length, "Массив содержит больше элемент чем должен");

        assertEquals(id, epics[0].getId(), "ID первой эпика не совпадает");
        assertEquals(id2, epics[1].getId(), "ID второй эпика не совпадает");
        assertEquals(epic.getName(), epics[0].getName(), "Имя первой эпика не совпадает");
        assertEquals(epic2.getName(), epics[1].getName(), "Имя второй эпика не совпадает");
    }

    @Test
    public void testUpdateEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Epic updateEpic = new Epic("Epic update name", "Epic update description", id);

        String taskJson = gson.toJson(updateEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/epics/1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();


        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        String body = response2.body();

        Epic updateEpicFromManager = gson.fromJson(body, Epic.class);

        ArrayList<Epic> epicList = (ArrayList<Epic>) manager.getEpicsList();
        assertEquals(1, epicList.size(), "Список содержит больше элемент чем должен");


        assertNotNull(updateEpicFromManager, "Эпики не возвращаются");
        assertNotEquals("Epic name", updateEpicFromManager.getName(), "Именна эпиков совподают");
        assertNotEquals("Epic description", updateEpicFromManager.getDescription(), "Описание эпиков совподают");
        assertEquals(TaskStatus.NEW, updateEpicFromManager.getStatus(), "Статусы эпиков не совподают");
        assertEquals(id, updateEpicFromManager.getId(), "ID эпиков не совподают");
    }

    @Test
    public void tesDeleteTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        assertNotNull(manager.getEpic(id));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Epic> EpicsList = (ArrayList<Epic>) manager.getEpicsList();

        assertEquals(0, EpicsList.size(), "Список содержит больше элемент чем должен");
        assertNull(manager.getEpic(id));
    }

    @Test
    public void tesDeleteAllTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic description");
        int id = manager.addNewEpic(epic);

        Epic epic2 = new Epic("Epic two name", "Epic two description");
        int id2 = manager.addNewEpic(epic2);

        Epic epic3 = new Epic("Epic three name", "Epic three description");
        int id3 = manager.addNewEpic(epic3);

        assertEquals(3, manager.getEpicsList().size(), "Список содержит больше элемент чем должен");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/all");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ArrayList<Epic> EpicsList = (ArrayList<Epic>) manager.getEpicsList();


        assertEquals(0, EpicsList.size(), "Список содержит больше элемент чем должен");
        assertNull(manager.getEpic(id));
        assertNull(manager.getEpic(id2));
        assertNull(manager.getEpic(id3));
    }
}
