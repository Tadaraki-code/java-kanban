package tests.tasks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import manager.*;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {
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
    public void epicsWithSameIdShouldBeEquals() {

        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);
        Epic epicOneUpdate = new Epic("New Name", "New description", epicOneId);
        Assertions.assertEquals(epicOne, epicOneUpdate);
    }

    @Test
    public void epicsShouldNotStoreTheIdOfSubtasksThatAreNotRelevant() {

        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 23, 19, 15));
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        Subtask subtaskThree = new Subtask("subtaskThree  name", "subtaskThree  description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 24, 19, 15));
        int subtaskThreeId = manager.addNewSubtask(subtaskThree);


        assertEquals(3, manager.getAllSubtask(epicOne).size());
        assertEquals(manager.getAllSubtask(epicOne).getFirst().getId(), subtaskOneId);
        assertEquals(manager.getAllSubtask(epicOne).get(1).getId(), subtaskTwoId);
        assertEquals(manager.getAllSubtask(epicOne).getLast().getId(), subtaskThreeId);

        manager.removeSubtask(subtaskOneId);
        assertEquals(2, manager.getAllSubtask(epicOne).size());
        assertEquals(manager.getAllSubtask(epicOne).getFirst().getId(), subtaskTwoId);
        assertEquals(manager.getAllSubtask(epicOne).getLast().getId(), subtaskThreeId);
    }


    @Test
    public void epicOnlyWithNewSubtasks() {
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(1, epic.getSubtaskList().size());

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 23, 19, 15));
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(3, epic.getSubtaskList().size());
    }

    @Test
    public void epicOnlyWithDoneSubtasks() {
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(1, epic.getSubtaskList().size());

        Subtask UpdateSubtask = new Subtask("subtaskOne name", "subtaskOne description", subtask.getId(),
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        manager.updateSubtask(UpdateSubtask);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 23, 19, 15));
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        assertEquals(epic.getStatus(), TaskStatus.DONE);
        assertEquals(3, epic.getSubtaskList().size());
    }

    //d. Подзадачи со статусом IN_PROGRESS.
    @Test
    public void epicWithDoneAndNewSubtasks() {
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(1, epic.getSubtaskList().size());

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtaskOne);


        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(2, epic.getSubtaskList().size());
    }

    @Test
    public void epicWithInProgressSubtasks() {
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(1, epic.getSubtaskList().size());

        Subtask UpdateSubtask = new Subtask("subtaskOne name", "subtaskOne description", subtask.getId(),
                TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        manager.updateSubtask(UpdateSubtask);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 23, 19, 15));
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(3, epic.getSubtaskList().size());
    }


}

