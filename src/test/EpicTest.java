package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import manager.*;
import tasks.Task;

class EpicTest {

    @Test
    public void EpicsWithSameIdShouldBeEquals() {
        TaskManager manager = Managers.getDefault();
        Epic epicOne = new Epic("Epic name", "Epic description");
        int epicOneId = manager.addNewEpic(epicOne);
        Epic epicOneUpdate = new Epic("New Name", "New description", epicOneId);
        Assertions.assertEquals(epicOne, epicOneUpdate);
    }


}

