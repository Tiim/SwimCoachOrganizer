package ch.tiim.sco.util.init;

import ch.tiim.sco.database.DatabaseController;
import javafx.concurrent.Task;

public class LoadDatabase extends Task<DatabaseController> {
    @Override
    protected DatabaseController call() throws Exception {
        DatabaseController db = new DatabaseController("./file.db");
        db.initializeDefaultValues();
        return db; //NON-NLS
    }
}
