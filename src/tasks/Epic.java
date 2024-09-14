package tasks;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskList = new ArrayList<>();
    private final TaskTypes type = TaskTypes.Epic;

    public Epic(String name, String description, int id) {
        super(name, description, id, TaskStatus.NEW);
    }

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
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
                '}';
    }


    @Override
    public TaskTypes getType() {
        return type;
    }
}
