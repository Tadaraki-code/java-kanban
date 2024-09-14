import tasks.*;
import manager.*;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {


//        manager.getSubtask(subtaskTwoId);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }

        File tempFile = null;

        try {
            tempFile = File.createTempFile("test",".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = Managers.getDefault(tempFile);


        Task taskOne = new Task("Task name", "Task description", TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);

        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);
        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW,epicOneId);
        Epic epicOneUpdate = new Epic("new Epic name","new Epic description",epicOneId);
        manager.updateEpic(epicOneUpdate);


        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskOneUpdate = new Subtask("subtaskOne name", "subtaskOne description",
                subtaskOneId,TaskStatus.DONE,epicOneId);
        manager.updateSubtask(subtaskOneUpdate);

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(tempFile);
        printAllTasks(manager2);

        tempFile.deleteOnExit();



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