package tests.tasks;

import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubtaskTest {

    TaskManager manager;
    Epic epic;
    Subtask subtask;
    File file;

    @BeforeEach
    public void setUp() throws IOException {
        this.file = new File("resources/test_" + System.nanoTime() + ".csv");
        this.manager = new FileBackedTaskManager(file);

        this.epic = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epic);

        this.subtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtask);
    }

    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void subtasksWithSameIdShouldBeEquals() {


        Subtask subtaskOneUpdate = new Subtask("New Name", "New description", subtask.getId(),
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));

        manager.updateSubtask(subtaskOneUpdate);
        assertEquals(subtask, subtaskOneUpdate);
    }
}