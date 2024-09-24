package tests.history;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.*;
import tasks.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager manager;
    File file;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    protected void setUp() {
        this.file = new File("resources/test_" + System.nanoTime() + ".csv");
        this.manager = new FileBackedTaskManager(file);

        this.task = new Task("Task name", "Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        int taskOneId = manager.addNewTask(task);

        this.epic = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epic);

        this.subtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15), LocalDateTime.of(2024, 9, 21, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtask);
    }

    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void savingThePreviousVersionOfTheTaskInHistoryManager() {
        manager.getTask(task.getId());

        Task updateTask = new Task("Task name changed", "Task description changed", task.getId(),
                TaskStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        manager.updateTask(updateTask);

        List<Task> historyList = manager.getHistory();

        assertNotEquals(historyList.getFirst().getName(), manager.getTask(task.getId()).getName());
        assertNotEquals(historyList.getFirst().getDescription(), manager.getTask(task.getId()).getDescription());
        assertEquals(historyList.getFirst().getId(), manager.getTask(task.getId()).getId());
        assertNotEquals(historyList.getFirst().getStatus(), manager.getTask(task.getId()).getStatus());
        assertNotNull(historyList.getFirst());
        assertNotNull(manager.getTask(task.getId()));
    }

    @Test
    public void checkEmptyHistoryList() {

        assertEquals(0, manager.getHistory().size());

        manager.getTask(task.getId());
        manager.getSubtask(subtask.getId());
        assertEquals(2, manager.getHistory().size());

        manager.cleanAllTasks();
        manager.cleanAllEpics();
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void checkingCorrectOperationAndRemovalFromLinkedList() {

        manager.getTask(task.getId());

        Task task2 = new Task("Task2 name", "Task2 description",
                TaskStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 7, 19, 19, 15));
        ;
        int task2Id = manager.addNewTask(task2);
        manager.getTask(task2Id);

        assertEquals(2, manager.getHistory().size());
        assertNotEquals(manager.getHistory().getFirst().getName(), task2.getName());
        assertNotEquals(manager.getHistory().getFirst().getDescription(), task2.getDescription());
        assertNotEquals(manager.getHistory().getFirst().getId(), task2.getId());
        assertNotEquals(manager.getHistory().getFirst().getStatus(), task2.getStatus());

        manager.getTask(task.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(), task.getName());
        assertEquals(manager.getHistory().getLast().getDescription(), task.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task.getId());
        assertEquals(manager.getHistory().getLast().getStatus(), task.getStatus());

        manager.removeTask(task.getId());

        assertEquals(1, manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(), task2.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(), task2.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task2.getId());
        assertEquals(manager.getHistory().getLast().getStatus(), task2.getStatus());

        manager.removeTask(task2Id);
        assertTrue(manager.getHistory().isEmpty());

        Task task3 = new Task("Task name3", "Task description3", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 12, 19, 19, 15));
        int task3Id = manager.addNewTask(task3);
        manager.getTask(task3Id);

        Task task4 = new Task("Task name4", "Task description4", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 1, 19, 19, 15));
        int task4Id = manager.addNewTask(task4);
        manager.getTask(task4Id);

        Task task5 = new Task("Task name5", "Task description5", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 2, 19, 19, 15));
        int task5Id = manager.addNewTask(task5);
        manager.getTask(task5Id);

        assertEquals(3, manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(), task3.getName());
        assertEquals(manager.getHistory().get(1).getDescription(), task4.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task5.getId());

        manager.removeTask(task4Id);

        assertEquals(2, manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(), task3.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(), task3.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(), task3.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(), task3.getStatus());

        assertEquals(2, manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(), task5.getName());
        assertEquals(manager.getHistory().getLast().getDescription(), task5.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task5.getId());
        assertEquals(manager.getHistory().getLast().getStatus(), task5.getStatus());

        manager.removeTask(task3Id);

        assertEquals(1, manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(), task5.getName());
        assertEquals(manager.getHistory().getLast().getDescription(), task5.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task5.getId());
        assertEquals(manager.getHistory().getLast().getStatus(), task5.getStatus());
        assertEquals(manager.getHistory().getFirst().getName(), task5.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(), task5.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(), task5.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(), task5.getStatus());
    }

    @Test
    public void checkForDuplicates() {
        manager.getTask(task.getId());
        manager.getTask(task.getId());
        assertEquals(1, manager.getHistory().size());

        manager.getEpic(epic.getId());
        manager.getTask(task.getId());
        assertEquals(2, manager.getHistory().size());

        assertEquals(manager.getHistory().getFirst().getName(), epic.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(), epic.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(), epic.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(), epic.getStatus());

        assertEquals(manager.getHistory().getLast().getName(), task.getName());
        assertEquals(manager.getHistory().getLast().getDescription(), task.getDescription());
        assertEquals(manager.getHistory().getLast().getId(), task.getId());
        assertEquals(manager.getHistory().getLast().getStatus(), task.getStatus());
    }
}