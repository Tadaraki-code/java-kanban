package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import manager.*;
import tasks.Subtask;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


class EpicTest {

    @Test
    public void EpicsWithSameIdShouldBeEquals() {
        TaskManager manager = Managers.getDefault();
        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);
        Epic epicOneUpdate = new Epic("New Name", "New description", epicOneId);
        Assertions.assertEquals(epicOne, epicOneUpdate);
    }

    @Test
    public void epicsShouldNotStoreTheIdOfSubtasksThatAreNotRelevant () {
        TaskManager manager = Managers.getDefault();

        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);

        Subtask subtaskOne = new Subtask("subtaskOne name", "subtaskOne description",
               TaskStatus.NEW,epicOneId);
       int subtaskOneId = manager.addNewSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("subtaskTwo name", "subtaskTwo description",
                TaskStatus.NEW,epicOneId);
        int subtaskTwoId = manager.addNewSubtask(subtaskTwo);

        Subtask subtaskThree = new Subtask("subtaskThree name", "subtaskThree description",
                TaskStatus.NEW,epicOneId);
        int subtaskThreeId = manager.addNewSubtask(subtaskThree);


        assertEquals(3,manager.getAllSubtask(epicOne).size());
        assertEquals(manager.getAllSubtask(epicOne).getFirst().getId(),subtaskOneId);
        assertEquals(manager.getAllSubtask(epicOne).get(1).getId(),subtaskTwoId);
        assertEquals(manager.getAllSubtask(epicOne).getLast().getId(),subtaskThreeId);

        manager.removeSubtask(subtaskOneId);
        assertEquals(2,manager.getAllSubtask(epicOne).size());
        assertEquals(manager.getAllSubtask(epicOne).getFirst().getId(),subtaskTwoId);
        assertEquals(manager.getAllSubtask(epicOne).getLast().getId(),subtaskThreeId);


    }


}

