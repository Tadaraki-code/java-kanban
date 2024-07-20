package test;

import org.junit.jupiter.api.Test;
import manager.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void ManagersShouldReturnReadyToWorkObjects () {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager.getTasksList());

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager.getHistory());

    }

}