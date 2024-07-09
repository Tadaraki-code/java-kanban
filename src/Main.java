import tasks.*;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        //Тесты класса Task
        Task task = new Task("Задача 1", "Проверка коректности", TaskStatus.NEW);
        final int taskOneId = manager.addNewTask(task);
        System.out.println(manager.getTask(taskOneId));

        Task task1 = manager.getTask(taskOneId);
        System.out.println(task.equals(task1));

        Task task2 = new Task("Задача 2", "Проверка коректности удаления", TaskStatus.NEW);
        final int taskTwoId = manager.addNewTask(task);

        manager.updateTask(new Task("Задача 1", "Проверка коректности", taskOneId, TaskStatus.IN_PROGRESS));
        System.out.println(manager.getTask(taskOneId));

        manager.updateTask(new Task("Задача 1", "Проверка коректности", taskOneId, TaskStatus.DONE));
        System.out.println(manager.getTask(taskOneId));

        System.out.println(manager.getTasksList());
        //manager.removeTask(taskOneId);
        //System.out.println(manager.getTasksList());
        manager.cleanAllTasks();
        System.out.println(manager.getTasksList());

        //Тесты класса Epic и Subtask
        System.out.println();
        System.out.println("Тест Epic и Subtask");

        Epic epic1 = new Epic("Епик 1", "Проверка работы");
        final int epicOneId = manager.addNewEpic(epic1);
        System.out.println(manager.getEpic(epicOneId));

        Epic epic2 = new Epic("Епик 2", "Удаление");
        final int epicTwoId = manager.addNewEpic(epic2);
        System.out.println(manager.getEpic(epicTwoId));

        Subtask subtask1 = new Subtask("Подзадача 1", "Проверка 1", TaskStatus.NEW, epicOneId);
        final int subtaskOneId = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Проверка 2", TaskStatus.NEW, epicOneId);
        final int subtaskTwoId = manager.addNewSubtask(subtask2);
        System.out.println(manager.getEpic(epicOneId));
        System.out.println(manager.getAllSubtask(epic1));
        System.out.println();

        Subtask subtask3 = new Subtask("Удаление", "Удалние", TaskStatus.NEW, epicTwoId);
        final int subtaskThreeId = manager.addNewSubtask(subtask3);
        System.out.println(manager.getEpic(epicTwoId));
        System.out.println(manager.getAllSubtask(epic2));
        System.out.println(manager.getSubtasksList());

        manager.updateSubtask(new Subtask("Удаление3", "Удалние", subtaskThreeId, TaskStatus.DONE, epicTwoId));
        System.out.println(epic2.getStatus());
        Subtask subtask4 = new Subtask("Проверка", "Проверка статуса", TaskStatus.NEW, epicTwoId);
        final int subtaskFourId = manager.addNewSubtask(subtask4);
        System.out.println(epic2.getStatus());
        manager.removeEpic(epicTwoId);

        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubtasksList());
        System.out.println();

        manager.updateSubtask(new Subtask("Подзадача 2", "Проверка 2", subtaskOneId, TaskStatus.DONE, epicOneId));
        System.out.println(epic1.getStatus());

        manager.removeSubtask(subtaskOneId);
        System.out.println(epic1.getStatus());

        manager.cleanAllSubtask();
        manager.cleanAllEpics();
        manager.printAllTasks();

        Epic testEpic = new Epic("Name", "Description");
        int testEpicId = manager.addNewEpic(testEpic);
        System.out.println(manager.getEpic(testEpicId));
        Subtask testSubtask = new Subtask("Test", "Test_test", TaskStatus.NEW, testEpicId);
        int testSubtaskId = manager.addNewSubtask(testSubtask);
        System.out.println(manager.getEpic(testEpicId));

        manager.updateSubtask(new Subtask("Test", "Test_test",
                testSubtaskId ,TaskStatus.IN_PROGRESS, testEpicId));
        System.out.println(manager.getEpic(testEpicId));

        manager.updateSubtask(new Subtask("Test", "Test_test",
                testSubtaskId ,TaskStatus.DONE, epicTwoId));

        manager.updateSubtask(new Subtask("Test", "Test_test",9 ,TaskStatus.DONE, epicTwoId));
        manager.updateSubtask(new Subtask("Test_Done", "Test_test_Done",
                testSubtaskId ,TaskStatus.DONE, testEpicId));
        System.out.println(manager.getSubtask(testSubtaskId));
        manager.updateEpic(new Epic("New Name", "New Description", testEpicId));
        System.out.println(manager.getEpic(testEpicId));
        manager.removeEpic(testEpicId);
        System.out.println(manager.getSubtask(testSubtaskId));
        System.out.println(manager.getEpic(testEpicId));


    }
}