package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int createId() {
        return ++id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    //Методы для класса Task
    @Override
    public int addNewTask(Task task) {

        if (task.getId() != 0) {
            final int uniqueId = task.getId();
            tasks.put(uniqueId, task);
            return uniqueId;
        }
        final int uniqueId = createId();
        task.setId(uniqueId);
        tasks.put(uniqueId, task);
        return uniqueId;
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) == null) {
            System.out.println("Такой задачи нет");
            return null;
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            System.out.println("Такой задачи нету, введите корректный id задачи для её обновления.");
            return;
        }
        tasks.put(task.getId(), task);

    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void cleanAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }


    //Методы для класс Epic
    @Override
    public int addNewEpic(Epic epic) {

        if (epic.getId() != 0) {
            final int uniqueId = epic.getId();
            epics.put(uniqueId, epic);
            updateEpicStatus(epic);
            return uniqueId;
        }
        final int uniqueId = createId();
        epic.setId(uniqueId);
        epics.put(uniqueId, epic);
        updateEpicStatus(epic);
        return uniqueId;
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) == null) {
            System.out.println("Такого эпика нет.");
            return epics.get(id);
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) == null) {
            System.out.println("Такого эпика нет.");
            return;
        }
        epics.get(epic.getId()).setName(epic.getName());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtaskIdList = epic.getSubtaskList();
        ArrayList<TaskStatus> subtaskStatus = new ArrayList<>();

        for (Integer integer : subtaskIdList) {
            if (subtasks.get(integer) != null) {
                subtaskStatus.add(subtasks.get(integer).getStatus());
            }
        }

        int[] statusCounter = {0, 0, 0};
        for (TaskStatus status : subtaskStatus) {
            switch (status) {
                case NEW -> statusCounter[0]++;
                case IN_PROGRESS -> statusCounter[1]++;
                case DONE -> statusCounter[2]++;
            }
        }


        if (statusCounter[0] == subtaskStatus.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusCounter[2] == subtaskStatus.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            ArrayList<Integer> subtasksId = epic.getSubtaskList();
            for (Integer subtaskId : subtasksId) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Такого этика нет.");
        }
    }

    @Override
    public void cleanAllEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask(Epic epic) {
        ArrayList<Integer> subtaskIdList = epic.getSubtaskList();
        ArrayList<Subtask> subTasksList = new ArrayList<>();

        for (Integer integer : subtaskIdList) {
            subTasksList.add(subtasks.get(integer));
        }
        return subTasksList;
    }


    //Методы для класса SubTask
    @Override
    public Integer addNewSubtask(Subtask subtask) {
        if (subtask.getId() != 0) {
            Integer epicId = subtask.getEpicId();
            final int uniqueId = subtask.getId();
            subtasks.put(uniqueId, subtask);
            epics.get(epicId).addSubtaskId(uniqueId);
            updateEpicStatus(epics.get(epicId));
            return uniqueId;
        }
        Integer epicId = subtask.getEpicId();
        final int uniqueId = createId();
        subtask.setId(uniqueId);
        subtasks.put(uniqueId, subtask);
        epics.get(epicId).addSubtaskId(uniqueId);
        updateEpicStatus(epics.get(epicId));
        return uniqueId;
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) == null) {
            System.out.println("Такой подзадачи нет!");
            return null;
        }
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.get(subtask.getId()) == null) {
            System.out.println("Такой подзадачи нет, введите коректный id подзадачи.");
            return;
        }
        if (subtasks.get(subtask.getId()).getEpicId() == subtask.getEpicId()) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Подзадача с таким id пренадлежит другому эпику,введитие коректный id эпика.");
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (subtasks.get(id) != null) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            ;
            subtasks.remove(id);
            epic.removeElementFromIdList(id);
            updateEpicStatus(epic);
            historyManager.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public void cleanAllSubtask() {
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskList();
            updateEpicStatus(epic);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void printAllTasks() {
        System.out.println("Все задачи" + getTasksList());
        System.out.println("Все эпики" + getEpicsList());
        System.out.println("Все подзадачи" + getSubtasksList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
