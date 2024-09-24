package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;
    private final TaskTypes type = TaskTypes.Subtask;

    public Subtask(String name, String description, int id, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

}
