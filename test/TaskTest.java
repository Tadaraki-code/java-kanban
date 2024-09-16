package test;
import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TaskTest {

    @Test
    public void tasksWithSameIdShouldBeEquals() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test",".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = Managers.getDefault(tempFile);

        Task taskOne = new Task("Task name", "Task description",TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        Task taskOneUpdate = new Task("New Name", "New description", taskOneId,TaskStatus.DONE);
        tempFile.deleteOnExit();
        manager.updateTask(taskOneUpdate);
     assertEquals(taskOne,taskOneUpdate);
    }


}