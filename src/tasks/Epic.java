package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, int id) {
        super(name, description, id, TaskStatus.NEW);
        setType(TaskTypes.Epic);
    }

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        setType(TaskTypes.Epic);
    }

    public void addSubtaskId(int id) {
        subtaskList.add(id);
    }

    public ArrayList<Integer> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    public void cleanSubtaskList() {
        subtaskList.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void removeElementFromIdList(Integer id) {
        subtaskList.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskList=" + subtaskList +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
