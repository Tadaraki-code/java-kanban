package tests.file;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TaskManagerTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadFromFileTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    public void setUp() throws IOException {

        file = new File("resources/test_" + System.nanoTime() + ".csv");
        taskManager = new FileBackedTaskManager(file);
        initTasks();
    }

    @AfterEach
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void loadFromFile() {

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file);

        assertEquals(taskManager.getTask(task.getId()), manager2.getTask(task.getId()));
        assertEquals(taskManager.getEpic(epic.getId()), manager2.getEpic(epic.getId()));
        assertEquals(taskManager.getSubtask(subtask.getId()), manager2.getSubtask(subtask.getId()));
        assertEquals(taskManager.getSubtask(subtask.getId()).getEpicId(), manager2.getSubtask(subtask.getId()).getEpicId());
        assertEquals(manager2.getEpic(epic.getId()).getSubtaskList().getFirst(), taskManager.getSubtask(subtask.getId()).getId());

    }

    @Test
    public void loadFromEmptyFile() {
        taskManager.cleanAllTasks();
        taskManager.cleanAllEpics();
        taskManager.cleanAllSubtask();

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertIterableEquals(taskManager.getTasksList(), manager2.getTasksList());
        Assertions.assertTrue(taskManager.getTasksList().isEmpty());
        Assertions.assertTrue(manager2.getTasksList().isEmpty());

    }
}