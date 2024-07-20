import tasks.*;
import manager.*;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
         TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("Task name", "Task description",TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        Task taskTwo = new Task("TaskTwo name", "Task description",TaskStatus.NEW);
        int taskTwoId = manager.addNewTask(taskTwo);

        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);

        Epic epicTwo = new Epic("EpicTwo name", "EpicTwo description");
        int epicTwoId = manager.addNewEpic(epicTwo);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW,epicOneId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.NEW,epicOneId);
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        Subtask subtaskThree = new Subtask("subtaskThree name", "subtaskThree description",
                TaskStatus.NEW,epicOneId);
        int subtaskThreeId = manager.addNewSubtask(subtaskThree);

        Subtask subtaskFour = new Subtask("subtaskThree name", "subtaskThree description",
                TaskStatus.NEW,epicTwoId);
        int subtaskFourId = manager.addNewSubtask(subtaskFour);

        printAllTasks(manager);

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        Subtask subtaskOneUpdate = new Subtask("subtaskOne name", "subtaskOne description",subtaskOneId,
                TaskStatus.IN_PROGRESS,epicOneId);
        manager.updateSubtask(subtaskOneUpdate);
        manager.getSubtask(subtaskOneId);
        manager.getEpic(epicOneId);

        manager.getEpic(epicTwoId);
        manager.removeEpic(epicTwoId);

        manager.getTask(taskOneId);
        manager.getTask(taskTwoId);
        manager.getSubtask(subtaskTwoId);


        printAllTasks(manager);

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();


        manager.removeTask(taskOneId);
        manager.removeSubtask(subtaskOneId);
        manager.getEpic(epicOneId);

        Subtask subtaskTwoUpdate = new Subtask("subtaskTwo name", "subtaskOne description",subtaskTwoId,
                TaskStatus.IN_PROGRESS,epicOneId);

        manager.updateSubtask(subtaskTwoUpdate);
        manager.getSubtask(subtaskTwoId);

        Epic epicOneUpdate = new Epic("Epic Update name", "Epic description",epicOneId);
        manager.updateEpic(epicOneUpdate);
        manager.getEpic(epicOneId);
        manager.removeSubtask(subtaskTwoId);
        printAllTasks(manager);


    }
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpicsList()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtask(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasksList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}