package data_structures_elements;

import tasks.Task;

public class Node {
     public Task task;
     public Node prev;
     public Node next;

    public Node(Node prev,Task task, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }
}
