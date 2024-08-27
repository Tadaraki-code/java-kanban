import tasks.*;
import manager.*;




public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();


        Task taskOne = new Task("Task name", "Task description",TaskStatus.NEW);
        int taskOneId = manager.addNewTask(taskOne);
        Task taskTwo = new Task("TaskTwo name", "Task description",TaskStatus.NEW);
        int taskTwoId = manager.addNewTask(taskTwo);

       Epic epicOne = new Epic("Epic name", "Epic description");
       int epicOneId = manager.addNewEpic(epicOne);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW,epicOneId);
        int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.NEW,epicOneId);
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        Subtask subtaskThree = new Subtask("subtaskThree name", "subtaskThree description",
               TaskStatus.NEW,epicOneId);
        int subtaskThreeId = manager.addNewSubtask(subtaskThree);

        Epic epicTwo = new Epic("EpicTwo name", "EpicTwo description");
        int epicTwoId = manager.addNewEpic(epicTwo);

        manager.getTask(taskOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getTask(taskTwoId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getEpic(epicOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getTask(taskOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getSubtask(subtaskOneId);
        manager.getSubtask(subtaskOneId);
        manager.getSubtask(subtaskOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getSubtask(subtaskTwoId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getSubtask(subtaskThreeId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getSubtask(subtaskOneId);
        manager.removeSubtask(subtaskThreeId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();


        manager.removeTask(taskTwoId);
        manager.getEpic(epicTwoId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.removeEpic(epicOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        manager.getTask(taskOneId);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();



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