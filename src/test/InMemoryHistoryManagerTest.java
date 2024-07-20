package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import manager.*;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void savingThePreviousVersionOfTheTaskInHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager manager = Managers.getDefault();
        Task taskOne = new Task("Task name", "Task description", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        manager.getTask(taskOneId);
        taskOne = new Task("Task name changed", "Task description changed",taskOneId ,TaskStatus.IN_PROGRESS);
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
    public void sizeOfHistoricalListShouldNotBeGreaterThan10() {
        HistoryManager historyManager = Managers.getDefaultHistory();
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

        assertEquals(10,manager.getHistory().size());
        assertEquals(manager.getHistory().getFirst().getName(),task2.getName());
        assertEquals(manager.getHistory().getFirst().getDescription(),task2.getDescription());
        assertEquals(manager.getHistory().getFirst().getId(),task2.getId());
        assertEquals(manager.getHistory().getFirst().getStatus(),task2.getStatus());

    }


}