package tests;


import org.junit.jupiter.api.Test;
import manager.*;
import tasks.*;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void savingThePreviousVersionOfTheTaskInHistoryManager() {
        TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("Task name", "Task description", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        manager.getTask(taskOneId);

        taskOne = new Task("Task name changed", "Task description changed", taskOneId, TaskStatus.IN_PROGRESS);
        manager.updateTask(taskOne);

        List<Task> historyList = manager.getHistory();

        assertNotEquals(historyList.getFirst().getName(),manager.getTask(taskOneId).getName());
        assertNotEquals(historyList.getFirst().getDescription(),manager.getTask(taskOneId).getDescription());
        assertEquals(historyList.getFirst().getId(),manager.getTask(taskOneId).getId());
        assertNotEquals(historyList.getFirst().getStatus(),manager.getTask(taskOneId).getStatus());
        assertNotNull(historyList.getFirst());
        assertNotNull(manager.getTask(taskOneId));
    }

    @Test
    public void sizeOfHistoricalListCanBeGreaterThan10() {

        TaskManager manager = Managers.getDefault();
        assertEquals(0,manager.getHistory().size());

        Task task1 = new Task("Task name1", "Task description", TaskStatus.NEW);
        int task1Id = manager.addNewTask(task1);
        manager.getTask(task1Id);
        assertEquals(1,manager.getHistory().size());


        Task task2 = new Task("Task name2", "Task description", TaskStatus.NEW);
        int task2Id = manager.addNewTask(task2);
        manager.getTask(task2Id);
        assertEquals(2,manager.getHistory().size());


        Task task3 = new Task("Task name3", "Task description", TaskStatus.NEW);
        int task3Id = manager.addNewTask(task3);
        manager.getTask(task3Id);
        assertEquals(3,manager.getHistory().size());

        Task task4 = new Task("Task name4", "Task description", TaskStatus.NEW);
        int task4Id = manager.addNewTask(task4);
        manager.getTask(task4Id);
        assertEquals(4,manager.getHistory().size());

        Task task5 = new Task("Task name5", "Task description", TaskStatus.NEW);
        int task5Id = manager.addNewTask(task5);
        manager.getTask(task5Id);
        assertEquals(5,manager.getHistory().size());

        Task task6 = new Task("Task name6", "Task description", TaskStatus.NEW);
        int task6Id = manager.addNewTask(task6);
        manager.getTask(task6Id);
        assertEquals(6,manager.getHistory().size());

        Task task7 = new Task("Task name7", "Task description", TaskStatus.NEW);
        int task7Id = manager.addNewTask(task7);
        manager.getTask(task7Id);
        assertEquals(7,manager.getHistory().size());

        Task task8 = new Task("Task name8", "Task description", TaskStatus.NEW);
        int task8Id = manager.addNewTask(task8);
        manager.getTask(task8Id);
        assertEquals(8,manager.getHistory().size());

        Task task9 = new Task("Task name9", "Task description", TaskStatus.NEW);
        int task9Id = manager.addNewTask(task9);
        manager.getTask(task9Id);
        assertEquals(9,manager.getHistory().size());

        Task task10 = new Task("Task name10", "Task description", TaskStatus.NEW);
        int task10Id = manager.addNewTask(task10);
        manager.getTask(task10Id);
        assertEquals(10,manager.getHistory().size());

        Task task11 = new Task("Task name11", "Task description", TaskStatus.NEW);
        int task11Id = manager.addNewTask(task11);
        manager.getTask(task11Id);
        assertEquals(11,manager.getHistory().size());

        Task task12 = new Task("Task name12", "Task description", TaskStatus.NEW);
        int task12Id = manager.addNewTask(task12);
        manager.getTask(task12Id);
        assertEquals(12,manager.getHistory().size());

        Task task13 = new Task("Task name13", "Task description", TaskStatus.NEW);
        int task13Id = manager.addNewTask(task13);
        manager.getTask(task13Id);
        assertEquals(13,manager.getHistory().size());

        Task task14 = new Task("Task name14", "Task description", TaskStatus.NEW);
        int task14Id = manager.addNewTask(task14);
        manager.getTask(task14Id);
        assertEquals(14,manager.getHistory().size());

        Task task15 = new Task("Task name15", "Task description", TaskStatus.NEW);
        int task15Id = manager.addNewTask(task15);
        manager.getTask(task15Id);
        assertEquals(15,manager.getHistory().size());


        assertEquals(15,manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(),task1.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(),task1.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(),task1.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(),task1.getStatus());

        Task task16 = new Task("Task name16", "Task description", TaskStatus.NEW);
        int task16Id = manager.addNewTask(task16);
        manager.getTask(task16Id);

        assertEquals(16,manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(),task16.getName());
        assertEquals(manager.getHistory().getLast().getDescription(),task16.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task16.getId());
        assertEquals(manager.getHistory().getLast().getStatus(),task16.getStatus());

    }

    @Test
    public void checkingCorrectOperationAndRemovalFromLinkedList() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task name1", "Task description", TaskStatus.NEW);
        int task1Id = manager.addNewTask(task1);
        manager.getTask(task1Id);

        Task task2 = new Task("Task name2", "Task description2", TaskStatus.IN_PROGRESS);
        int task2Id = manager.addNewTask(task2);
        manager.getTask(task2Id);

       assertEquals(2,manager.getHistory().size());
       assertNotEquals(manager.getHistory().getFirst().getName(),task2.getName());
       assertNotEquals(manager.getHistory().getFirst().getDescription(),task2.getDescription());
       assertNotEquals(manager.getHistory().getFirst().getId(),task2.getId());
       assertNotEquals(manager.getHistory().getFirst().getStatus(),task2.getStatus());

       manager.getTask(task1Id);

        assertEquals(2,manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(),task1.getName());
        assertEquals(manager.getHistory().getLast().getDescription(),task1.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task1.getId());
        assertEquals(manager.getHistory().getLast().getStatus(),task1.getStatus());

        manager.removeTask(task1Id);

        assertEquals(1,manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(),task2.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(),task2.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task2.getId());
        assertEquals(manager.getHistory().getLast().getStatus(),task2.getStatus());

        manager.removeTask(task2Id);
        assertTrue(manager.getHistory().isEmpty());

        Task task3 = new Task("Task name3", "Task description3", TaskStatus.NEW);
        int task3Id = manager.addNewTask(task3);
        manager.getTask(task3Id);

        Task task4 = new Task("Task name4", "Task description4", TaskStatus.NEW);
        int task4Id = manager.addNewTask(task4);
        manager.getTask(task4Id);

        Task task5 = new Task("Task name5", "Task description5", TaskStatus.NEW);
        int task5Id = manager.addNewTask(task5);
        manager.getTask(task5Id);

        assertEquals(3,manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(),task3.getName());
        assertEquals(manager.getHistory().get(1).getDescription(),task4.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task5.getId());

        manager.removeTask(task4Id);

        assertEquals(2,manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(),task3.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(),task3.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(),task3.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(),task3.getStatus());

        assertEquals(2,manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(),task5.getName());
        assertEquals(manager.getHistory().getLast().getDescription(),task5.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task5.getId());
        assertEquals(manager.getHistory().getLast().getStatus(),task5.getStatus());

        manager.removeTask(task3Id);

        assertEquals(1,manager.getHistory().size());
        assertEquals(manager.getHistory().getLast().getName(),task5.getName());
        assertEquals(manager.getHistory().getLast().getDescription(),task5.getDescription());
        assertEquals(manager.getHistory().getLast().getId(),task5.getId());
        assertEquals(manager.getHistory().getLast().getStatus(),task5.getStatus());
        assertEquals(manager.getHistory().getFirst().getName(),task5.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(),task5.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(),task5.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(),task5.getStatus());

    }
}