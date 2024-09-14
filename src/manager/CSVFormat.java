package manager;

import tasks.*;

public class CSVFormat {

    public static String taskToString (Task task) {
        String taskInfo;

        if(task.getType().equals(TaskTypes.Task) || task.getType().equals(TaskTypes.Epic)) {
            taskInfo = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + ",";
        }else if(task.getType().equals(TaskTypes.Subtask)) {
            Subtask subtask = (Subtask) task;
            taskInfo = subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId();
        }else {
            taskInfo = " ";
        }
        return taskInfo;
    }

    public static Task taskFromString(String line) {
        String[] taskInfo = line.split(",");
        Task task;

        if(taskInfo[1].equals("Task")) {
            task = new Task(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]), TaskStatus.valueOf(taskInfo[3]));
        }else if(taskInfo[1].equals("Epic")) {
            task = new Epic(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]));
        }else {
            task = new Subtask(taskInfo[2], taskInfo[4], Integer.parseInt(taskInfo[0]), TaskStatus.valueOf(taskInfo[3]),
                    Integer.parseInt(taskInfo[5]));
        }
        return task;
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
