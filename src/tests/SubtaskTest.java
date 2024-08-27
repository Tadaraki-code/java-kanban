package tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void subtasksWithSameIdShouldBeEquals() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.addNewEpic(epic);
        Subtask subtaskOne = new Subtask("Subtask name", "Subtask description",TaskStatus.NEW,epicId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskOneUpdate = new Subtask("New Name", "New description", subtaskOneId,TaskStatus.DONE, epicId);
        manager.updateSubtask(subtaskOneUpdate);
        assertEquals(subtaskOne, subtaskOneUpdate);
    }
}