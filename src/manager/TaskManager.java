package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.TreeSet;


public interface TaskManager {
    //Методы для класса Task
    int addNewTask(Task task);

    Task getTask(int id);

    boolean updateTask(Task task);

    void removeTask(int id);

    void cleanAllTasks();

    List<Task> getTasksList();

    //Методы для класс Epic
    int addNewEpic(Epic epic);

    Epic getEpic(int id);

    boolean updateEpic(Epic epic);

    void removeEpic(int id);

    void cleanAllEpics();

    List<Epic> getEpicsList();

    List<Subtask> getAllSubtask(Epic epic);

    //Методы для класса SubTask
    Integer addNewSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    boolean updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    void cleanAllSubtask();

    List<Subtask> getSubtasksList();

    void printAllTasks();

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTask();
}
