package test;

import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void subtasksWithSameIdShouldBeEquals() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test",".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = Managers.getDefault(tempFile);

        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.addNewEpic(epic);
        Subtask subtaskOne = new Subtask("Subtask name", "Subtask description",TaskStatus.NEW,epicId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskOneUpdate = new Subtask("New Name", "New description", subtaskOneId,TaskStatus.DONE, epicId);
        tempFile.deleteOnExit();

        manager.updateSubtask(subtaskOneUpdate);
        assertEquals(subtaskOne, subtaskOneUpdate);
    }
}