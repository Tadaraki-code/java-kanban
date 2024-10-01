package tests.tasks;

import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TaskTest {

    TaskManager manager;
    Task task;
    File file;

    @BeforeEach
    public void setUp() throws IOException {
        this.file = new File("resources/test_" + System.nanoTime() + ".csv");
        this.manager = new FileBackedTaskManager(file);

        this.task = new Task("Task name", "Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        int taskOneId = manager.addNewTask(task);
    }

    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void tasksWithSameIdShouldBeEquals() {

        Task taskOneUpdate = new Task("New Name", "New description", task.getId(),
                TaskStatus.DONE, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        manager.updateTask(taskOneUpdate);

        assertEquals(task, taskOneUpdate);
    }


}