package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 0;

    private int createId() {
        return ++id;
    }

    //Методы для класса Task
    public int addNewTask(Task task) {
        final int uniqueId = createId();
        task.setId(uniqueId);
        tasks.put(uniqueId, task);
        return uniqueId;
    }

    public Task getTask(int id) {
        if(tasks.get(id) == null) {
            System.out.println("Такой задачи нет");
            return null;
        }
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(),task);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void cleanAllTasks() {
        tasks.clear();
    }

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }


    //Методы для класс Epic
    public int addNewEpic(Epic epic) {
        final int uniqueId = createId();
        epic.setId(uniqueId);
        epics.put(uniqueId, epic);
        updateEpicStatus(epic);
        return uniqueId;
    }

    public Epic getEpic(int id) {
        if(epics.get(id) == null) {
            System.out.println("Такой задачи нет");
            return null;
        }
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(),epic);
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
       ArrayList<Integer> subtaskIdList = epic.getSubtaskList();
       ArrayList<TaskStatus> subtaskStatus = new ArrayList<>();

        for (Integer integer : subtaskIdList) {
            if (subtasks.get(integer) != null) {
                subtaskStatus.add(subtasks.get(integer).getStatus());
            }
        }

       int[] statusCounter = {0,0,0};
       for(TaskStatus status : subtaskStatus) {
           switch (status){
               case NEW -> statusCounter[0]++;
               case IN_PROGRESS -> statusCounter[1]++;
               case DONE -> statusCounter[2]++;
           }
       }

       if(statusCounter[0] == subtaskStatus.size()){
           epic.setStatus(TaskStatus.NEW);
       } else if (statusCounter[2] == subtaskStatus.size()) {
           epic.setStatus(TaskStatus.DONE);
       }else {
           epic.setStatus(TaskStatus.IN_PROGRESS);
       }
    }

    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        if(epic != null) {
            ArrayList<Integer> subtasksId = epic.getSubtaskList();
            for (Integer subtaskId : subtasksId) {
                subtasks.remove(subtaskId);
                epics.remove(id);
            }
        }else {
            System.out.println("Такого этика нет!");
        }
    }

    public void cleanAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtask(Epic epic) {
        ArrayList<Integer> subtaskIdList = epic.getSubtaskList();
        ArrayList<Subtask> subTasksList = new ArrayList<>();

        for(Integer integer: subtaskIdList){
            subTasksList.add(subtasks.get(integer));
        }
        return subTasksList;
    }


    //Методы для класса SubTask
    public Integer addNewSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if(epic == null) {
            System.out.println("Такого эпика нет!");
            return -1;
        }
        final int uniqueId = createId();
        subtask.setId(uniqueId);
        subtasks.put(uniqueId, subtask);
        epic.addSubtaskId(uniqueId);
        updateEpicStatus(epic);
        return uniqueId;
    }

    public Subtask getSubtask(int id) {
        if(subtasks.get(id) == null) {
            System.out.println("Такой задачи нет!");
            return null;
        }
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    public void cleanAllSubtask() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.cleanSubtaskList();
            updateEpicStatus(epic);
        }
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public void printAllTasks() {
        System.out.println("Все задачи" + getTasksList());
        System.out.println("Все эпики" + getEpicsList());
        System.out.println("Все подзадачи" + getSubtasksList());
    }




}
