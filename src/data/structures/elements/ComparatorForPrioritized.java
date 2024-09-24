package data.structures.elements;

import tasks.Task;

import java.util.Comparator;

public class ComparatorForPrioritized implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Comparator
                .comparing(Task::getId) // Сравнение по id
                .thenComparing(Task::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder())) // Затем по startTime
                .compare(t1, t2);
    }
}