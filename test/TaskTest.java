package test;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TaskTest {

    @Test
    public void tasksWithSameIdShouldBeEquals() {
        TaskManager manager = Managers.getDefault();
        Task taskOne = new Task("Task name", "Task description",TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        Task taskOneUpdate = new Task("New Name", "New description", taskOneId,TaskStatus.DONE);
        manager.updateTask(taskOneUpdate);
     assertEquals(taskOne,taskOneUpdate);
    }


}