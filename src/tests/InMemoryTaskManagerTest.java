package tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {

    @Test
    public void inMemoryTaskManagerShouldWorkCorrectly() {
        TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("Test task", "Test", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);

        assertEquals("Test",manager.getTask(taskOneId).getDescription());
        assertEquals("Test task",manager.getTask(taskOneId).getName());
        assertEquals(taskOneId,manager.getTask(taskOneId).getId());
        assertNotNull(manager.getTask(taskOneId));

        Epic epicOne = new Epic("Epic", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);

        assertEquals("Epic description",manager.getEpic(epicOneId).getDescription());
        assertEquals("Epic",manager.getEpic(epicOneId).getName());
        assertEquals(epicOneId,manager.getEpic(epicOneId).getId());
        assertNotNull(manager.getEpic(epicOneId));

        Subtask subtaskOne = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, epicOneId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        assertEquals("Subtask",manager.getSubtask(subtaskOneId).getName());
        assertEquals("Subtask description",manager.getSubtask(subtaskOneId).getDescription());
        assertEquals(subtaskOneId,manager.getSubtask(subtaskOneId).getId());
        assertNotNull(manager.getSubtask(subtaskOneId));
    }

    @Test
    public void allFieldsInAllTasksDoNotChangeWhenAdded() {
        TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("Test task", "Test", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);

        assertEquals("Test",manager.getTask(taskOneId).getDescription());
        assertEquals("Test task",manager.getTask(taskOneId).getName());
        assertEquals(taskOneId,manager.getTask(taskOneId).getId());
        assertEquals(TaskStatus.NEW,manager.getTask(taskOneId).getStatus());
        assertNotNull(manager.getTask(taskOneId));

        Epic epicOne = new Epic("Epic", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);

        assertEquals("Epic description",manager.getEpic(epicOneId).getDescription());
        assertEquals("Epic",manager.getEpic(epicOneId).getName());
        assertEquals(epicOneId,manager.getEpic(epicOneId).getId());
        assertEquals(TaskStatus.NEW,manager.getEpic(epicOneId).getStatus());
        assertEquals(0,manager.getEpic(epicOneId).getSubtaskList().size());
        assertNotNull(manager.getEpic(epicOneId));

        Subtask subtaskOne = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, epicOneId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        assertEquals("Subtask",manager.getSubtask(subtaskOneId).getName());
        assertEquals("Subtask description",manager.getSubtask(subtaskOneId).getDescription());
        assertEquals(subtaskOneId,manager.getSubtask(subtaskOneId).getId());
        assertEquals(TaskStatus.NEW,manager.getSubtask(subtaskOneId).getStatus());
        assertEquals(epicOneId,manager.getSubtask(subtaskOneId).getEpicId());
        assertNotNull(manager.getSubtask(subtaskOneId));
    }



}