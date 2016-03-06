package ch.tiim.sco.database;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class DatabaseTest {

    @Test
    public void testInMemory() throws SQLException, IOException {
        DatabaseController db = new DatabaseController(":memory:"); //NON-NLS
        db.close();
    }

    @Test
    @Ignore
    public void testFile() throws SQLException, IOException {
        Path p = Paths.get("./temp.db"); //NON-NLS
        try {
            DatabaseController db = new DatabaseController(p.toString());
            db.close();
        } finally {
            Files.deleteIfExists(p);
        }
    }
}
