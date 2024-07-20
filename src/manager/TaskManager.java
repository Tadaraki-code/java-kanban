package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Методы для класса Task
    int addNewTask(Task task);

    Task getTask(int id);

    void updateTask(Task task);

    void removeTask(int id);

    void cleanAllTasks();

    ArrayList<Task> getTasksList();

    //Методы для класс Epic
    int addNewEpic(Epic epic);

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    void cleanAllEpics();

    ArrayList<Epic> getEpicsList();

    ArrayList<Subtask> getAllSubtask(Epic epic);

    //Методы для класса SubTask
    Integer addNewSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    void cleanAllSubtask();

    ArrayList<Subtask> getSubtasksList();

    void printAllTasks();

    ArrayList<Task> getHistory();
}
