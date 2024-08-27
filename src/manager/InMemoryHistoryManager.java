package manager;

import data_structures_elements.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        remove(task.getId());
        Node newNode = linkLast(task);
        historyMap.put(task.getId(),newNode);
    }

    @Override
    public void remove(int id) {
        if(historyMap.get(id) == null) {
            return;
        }else {
            removeNode(historyMap.get(id));
        }
    }


    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) getTasks();
    }

    private Node linkLast(Task task) {
        Node last = this.last;
        Node newNode = new Node(last, task, null);
        this.last = newNode;
        if (last == null) {
            this.first = newNode;
        } else {
            last.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        if(first == null) {
            return tasksList;
        }else{
            Node node = first;
            while(node.next != null) {
                tasksList.add(node.task);
                node = node.next;
            }
                tasksList.add(node.task);

        }
        return tasksList;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        } else if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        } else if (node.prev == null && node.next != null) {
            this.first = node.next;
            node.next.prev = null;
        } else if (node.prev != null){
            this.last = node.prev;
            node.prev.next = null;
        }else {
            this.first = null;
            this.last = null;
        }
        historyMap.remove(node.task.getId());
    }
}
