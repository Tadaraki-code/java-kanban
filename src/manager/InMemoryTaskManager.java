package manager;

import data.structures.elements.ComparatorForPrioritized;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTask = new TreeSet<>(new ComparatorForPrioritized());

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
            addToPrioritizedSet(task);
            return uniqueId;
        }
        final int uniqueId = createId();
        task.setId(uniqueId);
        tasks.put(uniqueId, task);
        addToPrioritizedSet(task);
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
        prioritizedTask.remove(tasks.get(task.getId()));
        addToPrioritizedSet(task);
        tasks.put(task.getId(), task);

    }

    @Override
    public void removeTask(int id) {
        prioritizedTask.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void cleanAllTasks() {
        ArrayList<Integer> taskIds = new ArrayList<>(tasks.keySet());

        taskIds.forEach(id -> {
            historyManager.remove(id);  // Удаляем из менеджера истории
            prioritizedTask.remove(tasks.get(id));  // Удаляем из приоритетного списка
            tasks.remove(id);  // Удаляем саму задачу из коллекции tasks
        });
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
        calculateEpicTime(epic);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtaskIdList = epic.getSubtaskList();

        List<TaskStatus> subtaskStatus = subtaskIdList.stream()
                .filter(id -> subtasks.get(id) != null)
                .map(id -> subtasks.get(id).getStatus())
                .toList();


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

    private void calculateEpicTime(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtaskList();

        List<Task> tasks = prioritizedTask.stream()
                .filter(task -> epic.getSubtaskList().contains(task.getId()))
                .toList();

        if (!epic.getSubtaskList().isEmpty() & !tasks.isEmpty()) {

            epic.setStartTime(tasks.getFirst().getStartTime());
            epic.setEndTime(tasks.getLast().getEndTime());
            epic.setDuration(Duration.ofMinutes(subtasksId.stream()
                    .mapToLong(id -> subtasks.get(id).getDuration().toMinutes())
                    .sum()));
        } else {
            epic.setDuration(null);
            epic.setStartTime(null);
            epic.setEndTime(null);
        }
    }

    private boolean overlapCalculator(Task task) {
        boolean sameStartDate = prioritizedTask.stream()
                .noneMatch(task1 -> task1.getStartTime().equals(task.getStartTime()));

        boolean taskStartsWhenOtherTaskContinues = prioritizedTask.stream()
                .noneMatch(task1 -> task.getStartTime()
                        .isBefore(task1.getStartTime().plusMinutes(task1.getDuration().toMinutes())) &
                        task.getStartTime().isAfter(task1.getStartTime()));

        return sameStartDate & taskStartsWhenOtherTaskContinues;

    }

    @Override
    public void removeEpic(int id) {
        if (epics.get(id) != null) {
            epics.get(id).getSubtaskList().forEach(subtaskId -> {
                prioritizedTask.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });

            historyManager.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Такого этика нет.");
        }
    }

    @Override
    public void cleanAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        epics.clear();

        subtasks.keySet().forEach(subtaskId -> {
            prioritizedTask.remove(subtasks.get(subtaskId));
            historyManager.remove(subtaskId);
        });
        subtasks.clear();
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask(Epic epic) {
        return epic.getSubtaskList().stream()
                .map(subtasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
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
            addToPrioritizedSet(subtask);
            calculateEpicTime(epics.get(subtask.getEpicId()));
            return uniqueId;
        }
        Integer epicId = subtask.getEpicId();
        final int uniqueId = createId();
        subtask.setId(uniqueId);
        subtasks.put(uniqueId, subtask);
        epics.get(epicId).addSubtaskId(uniqueId);
        updateEpicStatus(epics.get(epicId));
        addToPrioritizedSet(subtask);
        calculateEpicTime(epics.get(subtask.getEpicId()));
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
            prioritizedTask.remove(subtasks.get(subtask.getId()));
            addToPrioritizedSet(subtask);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            calculateEpicTime(epic);
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
            prioritizedTask.remove(subtasks.get(id));
            subtasks.remove(id);
            epic.removeElementFromIdList(id);
            updateEpicStatus(epic);
            calculateEpicTime(epic);
            historyManager.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public void cleanAllSubtask() {
        subtasks.keySet().forEach(subtaskId -> {
            prioritizedTask.remove(subtasks.get(subtaskId));
            historyManager.remove(subtaskId);
        });
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.cleanSubtaskList();
            updateEpicStatus(epic);
            calculateEpicTime(epic);
        });
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

    public Set<Task> getPrioritizedTask() {
        return prioritizedTask;
    }

    private void addToPrioritizedSet(Task task) {
        if (task.getStartTime() != null & task.getDuration() != null) {
            if (overlapCalculator(task)) {
                prioritizedTask.add(task);
            } else {
                System.out.println("Введенное вами в задаче " + task.getName() + " с id " + task.getId() +
                        " время пересекается с уже существующей задачей! " +
                        "Эта задача не будет отображаться в списке приоритетов.");
            }
        } else {
            System.out.println("В задаче " + task.getName() + " с id " + task.getId() + " не установлено время! " +
                    "Эта задача не будет отображаться в списке приоритетов.");
        }

    }
}
