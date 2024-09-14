import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadFromFileTest {

    @Test
    public void loadFromFile() {

        try {
            File tempFile = File.createTempFile("test",".csv");
            FileBackedTaskManager manager = Managers.getDefault(tempFile);


            Task taskOne = new Task("Task name", "Task description", TaskStatus.NEW);
            int taskOneId = manager.addNewTask(taskOne);

            Epic epicOne = new Epic("Epic name", "Epic description");
            int epicOneId = manager.addNewEpic(epicOne);
            Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                    TaskStatus.NEW,epicOneId);
            int subtaskOneId = manager.addNewSubtask(subtaskOne);

            FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(tempFile);
            tempFile.deleteOnExit();


            Assertions.assertEquals(manager.getTask(taskOneId), manager2.getTask(taskOneId));
            Assertions.assertEquals(manager.getEpic(epicOneId), manager2.getEpic(epicOneId));
            Assertions.assertEquals(manager.getSubtask(subtaskOneId), manager2.getSubtask(subtaskOneId));
            Assertions.assertEquals(manager.getSubtask(subtaskOneId).getEpicId(), manager2.getSubtask(subtaskOneId).getEpicId());
            Assertions.assertEquals(manager2.getEpic(epicOneId).getSubtaskList().getFirst(),manager.getSubtask(subtaskOneId).getId());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void idTaskManagerMustBeTheSame() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = Managers.getDefault(tempFile);


        Task taskOne = new Task("Task name", "Task description", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);


        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);
        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epicOneId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(tempFile);

        Task taskTwo = new Task("TaskTwo name", "TaskTwo description", TaskStatus.NEW);
        int taskTwoId = manager2.addNewTask(taskTwo);
        tempFile.deleteOnExit();

        Assertions.assertEquals(4, manager2.getTask(taskTwoId).getId());
        Assertions.assertNull(manager.getSubtask(4));


    }
    @Test
    public void loadFromEmptyFile() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = Managers.getDefault(tempFile);


        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(tempFile);


        tempFile.deleteOnExit();

        Assertions.assertIterableEquals(manager.getTasksList(), manager2.getTasksList());
        Assertions.assertTrue(manager.getTasksList().isEmpty());
        Assertions.assertTrue(manager2.getTasksList().isEmpty());

    }
}
