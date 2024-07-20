package manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(historyList.size() == 10) {
            historyList.removeFirst();
            historyList.add(task);
        } else {
            historyList.add(task);
        }
    }


    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
