package manager;

import java.io.File;

public class Managers {

    private Managers() {
    }

    public static FileBackedTaskManager getDefault() {
        return new FileBackedTaskManager(new File("resources/tasks.csv"));
    }

    public static FileBackedTaskManager getDefault(File file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
