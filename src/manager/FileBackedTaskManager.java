package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String allTasksInfo = Files.readString(file.toPath());
            final String[] lines = allTasksInfo.split(System.lineSeparator());
            int counter = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (!line.isEmpty()) {
                    int id = addTask(line, taskManager);
                    if (counter < id) {
                        counter = id;
                    }
                }
            }
            taskManager.setId(counter);

        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать из файла" + file.getName(), e);
        }
        return taskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormat.getHeader());
            writer.newLine();

            for (Task task : super.getTasksList()) {
                writer.write(CSVFormat.taskToString(task));
                writer.newLine();
            }

            for (Epic epic : super.getEpicsList()) {
                writer.write(CSVFormat.taskToString(epic));
                writer.newLine();
            }

            for (Subtask subtask : super.getSubtasksList()) {
                writer.write(CSVFormat.taskToString(subtask));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно записать в файл" + file.getName(), e);
        }
    }

    private static int addTask(String line, FileBackedTaskManager taskManager) {
        String[] taskInfo = line.split(",");
        if (taskInfo[1].equals("Task")) {
            Task task = CSVFormat.taskFromString(line);
            taskManager.addNewTask(task);
        } else if (taskInfo[1].equals("Epic")) {
            Epic epic = (Epic) CSVFormat.taskFromString(line);
            taskManager.addNewEpic(epic);
        } else {
            Subtask subtask = (Subtask) CSVFormat.taskFromString(line);
            taskManager.addNewSubtask(subtask);
        }
        return Integer.parseInt(taskInfo[0]);
    }

    @Override
    public int addNewTask(Task task) {
        int id = task.getId();
        if (id == 0) {
            id = super.addNewTask(task);
            save();
            return id;
        }
        super.addNewTask(task);
        return id;
    }

    @Override
    public boolean updateTask(Task task) {
        if (super.updateTask(task)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void cleanAllTasks() {
        super.cleanAllTasks();
        save();
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = epic.getId();
        if (id == 0) {
            id = super.addNewEpic(epic);
            save();
            return id;
        }
        id = super.addNewEpic(epic);
        return id;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (super.updateEpic(epic)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void cleanAllEpics() {
        super.cleanAllEpics();
        save();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (id == 0) {
            id = super.addNewSubtask(subtask);
            save();
            return id;
        }
        id = super.addNewSubtask(subtask);
        return id;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (super.updateSubtask(subtask)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void cleanAllSubtask() {
        super.cleanAllSubtask();
        save();
    }
}
