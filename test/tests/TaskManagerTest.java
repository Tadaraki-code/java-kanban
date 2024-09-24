package tests;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;

    protected Epic epic;

    protected Subtask subtask;

    protected void initTasks() {

        this.task = new Task("Task name", "Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));
        int taskOneId = taskManager.addNewTask(task);

        this.epic = new Epic("Epic name", "Epic description");
        int epicOneId = taskManager.addNewEpic(epic);

        this.subtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epicOneId, Duration.ofMinutes(15), LocalDateTime.of(2024, 9, 21, 19, 15));
        int subtaskOneId = taskManager.addNewSubtask(subtask);
    }

    @Test
    void addNewTask() {
        Task secondTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 20, 19, 15));
        int secondTaskId = taskManager.addNewTask(secondTask);

        assertEquals("Second Task name", taskManager.getTask(secondTaskId).getName());
        assertEquals("Second Task description", taskManager.getTask(secondTaskId).getDescription());
        assertEquals(secondTaskId, taskManager.getTask(secondTaskId).getId());
        assertNotNull(taskManager.getTask(secondTaskId));
        assertEquals(2, taskManager.getTasksList().size());

    }

    @Test
    void getTask() {
        assertEquals("Task name", taskManager.getTask(task.getId()).getName());
        assertEquals("Task description", taskManager.getTask(task.getId()).getDescription());
        assertEquals(task.getId(), taskManager.getTask(task.getId()).getId());
        assertNotNull(taskManager.getTask(task.getId()));
    }

    @Test
    void updateTask() {
        Task updateTask = new Task("Task name update", "Task description update", task.getId(),
                TaskStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 19, 19, 15));

        taskManager.updateTask(updateTask);
        assertEquals(updateTask.getName(), taskManager.getTask(task.getId()).getName());
        assertEquals(updateTask.getDescription(), taskManager.getTask(task.getId()).getDescription());
        assertEquals(updateTask.getStatus(), taskManager.getTask(task.getId()).getStatus());
        assertEquals(updateTask.getId(), taskManager.getTask(task.getId()).getId());
        assertNotNull(taskManager.getTask(task.getId()));
        assertEquals(1, taskManager.getTasksList().size());
    }

    @Test
    void removeTask() {
        int id = task.getId();
        assertNotNull(taskManager.getTask(id));
        assertEquals(1, taskManager.getTasksList().size());
        taskManager.removeTask(id);
        assertNull(taskManager.getTask(id));
        assertEquals(0, taskManager.getTasksList().size());
    }

    @Test
    void cleanAllTasks() {
        Task secondTask = new Task("Second Task name", "Second Task description", TaskStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 20, 19, 15));
        int secondTaskId = taskManager.addNewTask(secondTask);
        assertEquals(2, taskManager.getTasksList().size());
        taskManager.cleanAllTasks();
        assertEquals(0, taskManager.getTasksList().size());
    }

    @Test
    void getTasksList() {
        List<Task> taskList = taskManager.getTasksList();
        assertEquals(task.getName(), taskList.getFirst().getName());
        assertEquals(task.getDescription(), taskList.getFirst().getDescription());
        assertEquals(task.getId(), taskList.getFirst().getId());
        assertEquals(task.getStatus(), taskList.getFirst().getStatus());
        assertEquals(1, taskList.size());
    }

    @Test
    void addNewEpic() {
        Epic secondEpic = new Epic("Second Epic name", "Second Epic description");
        int epicTwoId = taskManager.addNewEpic(secondEpic);

        assertEquals(secondEpic.getName(), taskManager.getEpic(epicTwoId).getName());
        assertEquals(secondEpic.getDescription(), taskManager.getEpic(epicTwoId).getDescription());
        assertEquals(secondEpic.getId(), taskManager.getEpic(epicTwoId).getId());
        assertEquals(secondEpic.getStatus(), taskManager.getEpic(epicTwoId).getStatus());
        assertEquals(2, taskManager.getEpicsList().size());
    }

    @Test
    void getEpic() {
        assertEquals(epic.getName(), taskManager.getEpic(epic.getId()).getName());
        assertEquals(epic.getDescription(), taskManager.getEpic(epic.getId()).getDescription());
        assertEquals(epic.getId(), taskManager.getEpic(epic.getId()).getId());
        assertEquals(epic.getStatus(), taskManager.getEpic(epic.getId()).getStatus());
        assertNotNull(taskManager.getEpic(epic.getId()));
    }

    @Test
    void updateEpic() {
        Epic updateEpic = new Epic("Update Epic name", "Update Epic description", epic.getId());

        taskManager.updateEpic(updateEpic);
        assertEquals(taskManager.getEpic(epic.getId()).getName(), updateEpic.getName());
        assertEquals(taskManager.getEpic(epic.getId()).getDescription(), updateEpic.getDescription());
        assertEquals(taskManager.getEpic(epic.getId()).getId(), updateEpic.getId());
        assertNotNull(taskManager.getEpic(epic.getId()));
        assertEquals(1, taskManager.getEpicsList().size());

    }

    @Test
    void removeEpic() {
        int id = epic.getId();
        assertNotNull(taskManager.getEpic(id));
        assertEquals(1, taskManager.getEpicsList().size());
        taskManager.removeEpic(id);
        assertNull(taskManager.getEpic(id));
        assertEquals(0, taskManager.getEpicsList().size());
    }

    @Test
    void cleanAllEpics() {
        Epic secondEpic = new Epic("Second Epic name", "Second Epic description");
        int secondEpicId = taskManager.addNewEpic(secondEpic);
        assertEquals(2, taskManager.getEpicsList().size());
        assertEquals(1, taskManager.getSubtasksList().size());
        taskManager.cleanAllEpics();
        assertEquals(0, taskManager.getEpicsList().size());
        assertEquals(0, taskManager.getSubtasksList().size());
    }

    @Test
    void getEpicsList() {
        List<Epic> taskList = taskManager.getEpicsList();
        assertEquals(epic.getName(), taskList.getFirst().getName());
        assertEquals(epic.getDescription(), taskList.getFirst().getDescription());
        assertEquals(epic.getId(), taskList.getFirst().getId());
        assertEquals(epic.getStatus(), taskList.getFirst().getStatus());
        assertEquals(epic.getSubtaskList().getFirst(), subtask.getId());
        assertEquals(1, taskList.size());
    }

    @Test
    void getAllSubtask() {
        Subtask subtask1 = taskManager.getSubtask(epic.getSubtaskList().getFirst());
        assertEquals(subtask.getName(), subtask1.getName());
        assertEquals(subtask.getDescription(), subtask1.getDescription());
        assertEquals(subtask.getId(), subtask1.getId());
        assertEquals(subtask.getStatus(), subtask1.getStatus());
        assertEquals(subtask.getEpicId(), subtask1.getEpicId());
    }

    @Test
    void addNewSubtask() {
        Subtask secondSubtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        int secondSubtaskId = taskManager.addNewSubtask(secondSubtask);

        assertEquals(secondSubtask.getName(), taskManager.getSubtask(secondSubtaskId).getName());
        assertEquals(secondSubtask.getDescription(), taskManager.getSubtask(secondSubtaskId).getDescription());
        assertEquals(secondSubtask.getId(), taskManager.getSubtask(secondSubtaskId).getId());
        assertEquals(secondSubtask.getStatus(), taskManager.getSubtask(secondSubtaskId).getStatus());
        assertEquals(secondSubtask.getEpicId(), taskManager.getSubtask(secondSubtaskId).getEpicId());
        assertEquals(2, epic.getSubtaskList().size());
    }

    @Test
    void getSubtask() {
        Subtask subtask1 = taskManager.getSubtask(subtask.getId());

        assertEquals(subtask.getName(), subtask1.getName());
        assertEquals(subtask.getDescription(), subtask1.getDescription());
        assertEquals(subtask.getId(), subtask1.getId());
        assertEquals(subtask.getStatus(), subtask1.getStatus());
        assertEquals(subtask.getEpicId(), subtask1.getEpicId());

    }

    @Test
    void updateSubtask() {
        Subtask updateSubtask = new Subtask("subtaskOne name", "subtaskOne description", subtask.getId(),
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        taskManager.updateSubtask(updateSubtask);

        assertEquals(taskManager.getSubtask(subtask.getId()).getName(), updateSubtask.getName());
        assertEquals(taskManager.getSubtask(subtask.getId()).getDescription(), updateSubtask.getDescription());
        assertEquals(taskManager.getSubtask(subtask.getId()).getId(), updateSubtask.getId());
        assertEquals(taskManager.getSubtask(subtask.getId()).getStatus(), updateSubtask.getStatus());
        assertEquals(taskManager.getSubtask(subtask.getId()).getEpicId(), updateSubtask.getEpicId());
        assertEquals(1, taskManager.getSubtasksList().size());
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void removeSubtask() {
        int id = subtask.getId();
        assertNotNull(taskManager.getSubtask(id));
        assertEquals(1, taskManager.getSubtasksList().size());
        taskManager.removeSubtask(id);
        assertNull(taskManager.getSubtask(id));
        assertEquals(0, taskManager.getSubtasksList().size());

    }

    @Test
    void cleanAllSubtask() {
        Subtask secondSubtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        int secondSubtaskId = taskManager.addNewSubtask(secondSubtask);

        assertEquals(2, taskManager.getSubtasksList().size());
        taskManager.cleanAllSubtask();
        assertEquals(0, taskManager.getSubtasksList().size());


    }

    @Test
    void getSubtasksList() {
        List<Subtask> taskList = taskManager.getSubtasksList();
        assertEquals(subtask.getName(), taskList.getFirst().getName());
        assertEquals(subtask.getDescription(), taskList.getFirst().getDescription());
        assertEquals(subtask.getId(), taskList.getFirst().getId());
        assertEquals(subtask.getStatus(), taskList.getFirst().getStatus());
        assertEquals(subtask.getEpicId(), taskList.getFirst().getEpicId());
        assertEquals(1, taskList.size());
    }


    @Test
    void getHistory() {
        List<Task> historyList = taskManager.getHistory();
        assertEquals(0, historyList.size());
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getTask(task.getId());
        historyList = taskManager.getHistory();

        assertEquals(2, historyList.size());
        assertEquals(historyList.getFirst().getType(), epic.getType());
        assertEquals(historyList.getFirst().getName(), epic.getName());
        assertEquals(historyList.getFirst().getId(), epic.getId());

        assertEquals(historyList.getLast().getType(), task.getType());
        assertEquals(historyList.getLast().getName(), task.getName());
        assertEquals(historyList.getLast().getId(), task.getId());
    }

    @Test
    void prioritizedTaskListShouldWorkCorrectly() {
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(subtask.getStatus(), TaskStatus.NEW);
        assertEquals(1, epic.getSubtaskList().size());

        Subtask secondSubtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int secondSubtaskId = taskManager.addNewSubtask(secondSubtask);

        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(taskManager.getSubtask(subtask.getId()).getStatus(), TaskStatus.NEW);
        assertEquals(taskManager.getSubtask(secondSubtaskId).getStatus(), TaskStatus.DONE);
        assertEquals(2, epic.getSubtaskList().size());

        Subtask updateSubtask = new Subtask("subtaskOne name", "subtaskOne description", subtask.getId(),
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 21, 19, 15));
        taskManager.updateSubtask(updateSubtask);

        assertEquals(epic.getStatus(), TaskStatus.DONE);
        assertEquals(taskManager.getSubtask(subtask.getId()).getStatus(), TaskStatus.DONE);
        assertEquals(taskManager.getSubtask(secondSubtaskId).getStatus(), TaskStatus.DONE);
        assertEquals(2, epic.getSubtaskList().size());

        taskManager.cleanAllSubtask();
        assertEquals(epic.getStatus(), TaskStatus.NEW);
        assertEquals(0, epic.getSubtaskList().size());
        assertEquals(0, taskManager.getSubtasksList().size());

    }

    @Test
    void calculateEpicTime() {
        Subtask secondSubtask = new Subtask("subtaskOne name", "subtaskOne description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 22, 19, 15));
        int secondSubtaskId = taskManager.addNewSubtask(secondSubtask);

        Subtask thirdSubtask = new Subtask("subtask third name", "subtask third description",
                TaskStatus.NEW, epic.getId(), Duration.ofMinutes(15),
                LocalDateTime.of(2024, 9, 23, 19, 15));
        int thirdSubtaskId = taskManager.addNewSubtask(thirdSubtask);

        assertEquals(epic.getStartTime(), subtask.getStartTime());
        assertEquals(epic.getEndTime(), thirdSubtask.getEndTime());
        assertEquals(45, epic.getDuration().toMinutes());
    }
}
