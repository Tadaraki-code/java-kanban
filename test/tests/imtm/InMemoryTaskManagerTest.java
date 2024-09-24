package tests.imtm;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import tests.TaskManagerTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    void overlapCalculator() {
        Set<Task> prioritize = taskManager.getPrioritizedTask();
        List<Task> prioritizeList = new ArrayList<>(prioritize);
        assertEquals(2, prioritize.size());
        assertEquals(1, taskManager.getTasksList().size());

        assertEquals(prioritizeList.getFirst().getId(), task.getId());
        assertEquals(prioritizeList.getFirst().getType(), task.getType());
        assertEquals(prioritizeList.getFirst().getName(), task.getName());

        assertEquals(prioritizeList.getLast().getId(), subtask.getId());
        assertEquals(prioritizeList.getLast().getType(), subtask.getType());
        assertEquals(prioritizeList.getLast().getName(), subtask.getName());

        Task secondTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        int taskSecondId = taskManager.addNewTask(secondTask);
        assertEquals(-1,taskSecondId);

        prioritize = taskManager.getPrioritizedTask();
        prioritizeList = new ArrayList<>(prioritize);
        assertEquals(2, prioritize.size());
        assertEquals(1, taskManager.getTasksList().size());

        assertEquals(prioritizeList.getFirst().getId(), task.getId());
        assertEquals(prioritizeList.getFirst().getType(), task.getType());
        assertEquals(prioritizeList.getFirst().getName(), task.getName());

        assertEquals(prioritizeList.getLast().getId(), subtask.getId());
        assertEquals(prioritizeList.getLast().getType(), subtask.getType());
        assertEquals(prioritizeList.getLast().getName(), subtask.getName());

        Task updateSecondTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 19, 19, 15));
        taskSecondId = taskManager.addNewTask(updateSecondTask);

        prioritize = taskManager.getPrioritizedTask();
        prioritizeList = new ArrayList<>(prioritize);
        assertEquals(3, prioritize.size());
        assertEquals(2, taskManager.getTasksList().size());

        assertEquals(prioritizeList.getFirst().getId(), task.getId());
        assertEquals(prioritizeList.getFirst().getType(), task.getType());
        assertEquals(prioritizeList.getFirst().getName(), task.getName());

        assertEquals(prioritizeList.get(1).getId(), subtask.getId());
        assertEquals(prioritizeList.get(1).getType(), subtask.getType());
        assertEquals(prioritizeList.get(1).getName(), subtask.getName());

        assertEquals(prioritizeList.getLast().getId(), updateSecondTask.getId());
        assertEquals(prioritizeList.getLast().getType(), updateSecondTask.getType());
        assertEquals(prioritizeList.getLast().getName(), updateSecondTask.getName());

        taskManager.removeSubtask(subtask.getId());
        prioritize = taskManager.getPrioritizedTask();
        prioritizeList = new ArrayList<>(prioritize);
        assertEquals(2, prioritize.size());

        assertEquals(prioritizeList.getFirst().getId(), task.getId());
        assertEquals(prioritizeList.getFirst().getType(), task.getType());
        assertEquals(prioritizeList.getFirst().getName(), task.getName());

        assertEquals(prioritizeList.get(1).getId(), updateSecondTask.getId());
        assertEquals(prioritizeList.get(1).getType(), updateSecondTask.getType());
        assertEquals(prioritizeList.get(1).getName(), updateSecondTask.getName());

        Task updateTask = new Task("Update name Task", "Update Task dis",
                task.getId(), TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 19, 19, 15));
        taskManager.updateTask(updateTask);

        prioritize = taskManager.getPrioritizedTask();
        prioritizeList = new ArrayList<>(prioritize);
        assertEquals(2, prioritize.size());
        System.out.println(prioritize);
        assertEquals(2, taskManager.getTasksList().size());
        assertEquals(prioritizeList.getFirst().getId(), task.getId());
        assertEquals(prioritizeList.getFirst().getType(), task.getType());
        assertEquals(prioritizeList.getFirst().getName(), task.getName());

        assertEquals(prioritizeList.getLast().getId(), updateSecondTask.getId());
        assertEquals(prioritizeList.getLast().getType(), updateSecondTask.getType());
        assertEquals(prioritizeList.getLast().getName(), updateSecondTask.getName());
    }

    @Test
    public void inMemoryTaskManagerShouldWorkCorrectly() {

        Task secondTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 18, 19, 15));
        int taskSecondId = taskManager.addNewTask(secondTask);

        assertEquals("Second Task description", taskManager.getTask(taskSecondId).getDescription());
        assertEquals("Second Task name", taskManager.getTask(taskSecondId).getName());
        assertEquals(taskSecondId, taskManager.getTask(taskSecondId).getId());
        assertNotNull(taskManager.getTask(taskSecondId));

        Epic epicOne = new Epic("Epic", "Epic description");
        int epicOneId = taskManager.addNewEpic(epicOne);

        assertEquals("Epic description", taskManager.getEpic(epicOneId).getDescription());
        assertEquals("Epic", taskManager.getEpic(epicOneId).getName());
        assertEquals(epicOneId, taskManager.getEpic(epicOneId).getId());
        assertNotNull(taskManager.getEpic(epicOneId));

        Subtask subtaskOne = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 21, 15));
        int subtaskOneId = taskManager.addNewSubtask(subtaskOne);

        assertEquals("Subtask", taskManager.getSubtask(subtaskOneId).getName());
        assertEquals("Subtask description", taskManager.getSubtask(subtaskOneId).getDescription());
        assertEquals(subtaskOneId, taskManager.getSubtask(subtaskOneId).getId());
        assertNotNull(taskManager.getSubtask(subtaskOneId));
    }

    @Test
    public void allFieldsInAllTasksDoNotChangeWhenAdded() {

        Task oneTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 20, 19, 15));
        int taskOneId = taskManager.addNewTask(oneTask);

        assertEquals("Second Task description", taskManager.getTask(taskOneId).getDescription());
        assertEquals("Second Task name", taskManager.getTask(taskOneId).getName());
        assertEquals(taskOneId, taskManager.getTask(taskOneId).getId());
        assertEquals(TaskStatus.NEW, taskManager.getTask(taskOneId).getStatus());
        assertEquals(oneTask.getStartTime(), taskManager.getTask(taskOneId).getStartTime());
        assertEquals(oneTask.getDuration(), taskManager.getTask(taskOneId).getDuration());
        assertNotNull(taskManager.getTask(taskOneId));

        Epic epicOne = new Epic("Epic", "Epic description");
        int epicOneId = taskManager.addNewEpic(epicOne);

        assertEquals("Epic description", taskManager.getEpic(epicOneId).getDescription());
        assertEquals("Epic", taskManager.getEpic(epicOneId).getName());
        assertEquals(epicOneId, taskManager.getEpic(epicOneId).getId());
        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicOneId).getStatus());
        assertEquals(0, taskManager.getEpic(epicOneId).getSubtaskList().size());
        assertNotNull(taskManager.getEpic(epicOneId));

        Subtask subtaskOne = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, epicOneId, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 11, 19, 19, 15));
        int subtaskOneId = taskManager.addNewSubtask(subtaskOne);

        assertEquals("Subtask", taskManager.getSubtask(subtaskOneId).getName());
        assertEquals("Subtask description", taskManager.getSubtask(subtaskOneId).getDescription());
        assertEquals(subtaskOneId, taskManager.getSubtask(subtaskOneId).getId());
        assertEquals(TaskStatus.NEW, taskManager.getSubtask(subtaskOneId).getStatus());
        assertEquals(epicOneId, taskManager.getSubtask(subtaskOneId).getEpicId());
        assertEquals(subtaskOne.getStartTime(), taskManager.getSubtask(subtaskOneId).getStartTime());
        assertEquals(subtaskOne.getDuration(), taskManager.getSubtask(subtaskOneId).getDuration());
        assertNotNull(taskManager.getSubtask(subtaskOneId));
    }
}