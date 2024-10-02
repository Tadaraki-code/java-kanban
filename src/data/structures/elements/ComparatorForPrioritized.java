package data.structures.elements;

import tasks.Task;

import java.util.Comparator;

public class ComparatorForPrioritized implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Comparator
                .comparing((Task t) -> t.getDuration() != null ? t.getDuration().toMinutes() : Long.MAX_VALUE)
                .thenComparing(Task::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder()))
                .compare(t1, t2);
    }
}