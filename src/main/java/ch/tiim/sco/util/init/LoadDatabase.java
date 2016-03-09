package ch.tiim.sco.util.init;

import ch.tiim.sco.database.DatabaseController;
import javafx.concurrent.Task;

public class LoadDatabase extends Task<DatabaseController> {
    @Override
    protected DatabaseController call() throws Exception {
        updateProgress(0, 10);
        DatabaseController db = new DatabaseController("./file.db"); //NON-NLS
        updateProgress(5, 10);
        db.initializeDefaultValues();
        updateProgress(10, 10);
        return db;
    }
}
