package manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import tasks.*;

public class CSVFormat {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm");

    public static String taskToString(Task task) {
        String taskInfo;

        if (task.getType().equals(TaskTypes.Task)) {
            taskInfo = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + "," +
                    (task.getDuration() != null ? task.getDuration().toMinutes() : null) +
                    "," + (task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : null);
        } else if (task.getType().equals(TaskTypes.Epic)) {
            taskInfo = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription();
        } else if (task.getType().equals(TaskTypes.Subtask)) {
            Subtask subtask = (Subtask) task;
            taskInfo = subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId() + "," +
                    (task.getDuration() != null ? task.getDuration().toMinutes() : null) + "," +
                    (task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : null);
        } else {
            taskInfo = " ";
        }
        return taskInfo;
    }

    public static Task taskFromString(String line) {
        String[] taskInfo = line.split(",");
        Task task;

        if (taskInfo[1].equals("Task")) {
            task = new Task(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]), TaskStatus.valueOf(taskInfo[3]),
                    (taskInfo[5].equals("null") ? null : Duration.ofMinutes(Long.parseLong(taskInfo[5]))),
                    (taskInfo[6].equals("null") ? null : LocalDateTime.parse(taskInfo[6], FORMATTER)));
        } else if (taskInfo[1].equals("Epic")) {
            task = new Epic(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]));
        } else {
            task = new Subtask(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]), TaskStatus.valueOf(taskInfo[3]),
                    Integer.parseInt(taskInfo[5]),
                    (taskInfo[6].equals("null") ? null : Duration.ofMinutes(Long.parseLong(taskInfo[6]))),
                    (taskInfo[7].equals("null") ? null : LocalDateTime.parse(taskInfo[7], FORMATTER)));
        }
        return task;
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
