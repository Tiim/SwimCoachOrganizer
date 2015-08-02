package ch.tiim.trainingmanager.database;

import ch.tiim.log.Log;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseController implements Closeable {
    private static final Log LOGGER = new Log(DatabaseController.class);
    private static final int VERSION = 1;

    private final TableSetFocus tblSetFocus;
    private final TableSetForm tblSetForm;
    private final TableTraining tblTraining;
    private final TableTrainingContent tblTrainingContent;
    private final TableSets tblSet;
    private final Connection conn;


    public DatabaseController(String file) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("org.sqlite.JDBC not found!");
        }
        boolean notExists = !Files.exists(Paths.get(file));
        conn = DriverManager.getConnection("jdbc:sqlite:" + file);

        if (notExists) {
            conn.createStatement().executeUpdate("PRAGMA user_version = " + VERSION);
        }
        conn.createStatement().executeUpdate("PRAGMA foreign_keys = ON");
        List<Table> tables = new ArrayList<>();

        tables.addAll(Arrays.asList(
                tblSetFocus = new TableSetFocus(this, notExists),
                tblSetForm = new TableSetForm(this, notExists),
                tblTraining = new TableTraining(this, notExists),
                tblSet = new TableSets(this, notExists),
                tblTrainingContent = new TableTrainingContent(this, notExists)
        ));
        try {
            if (notExists) {
                for (Table t : tables) t.mkTable();
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
            conn.close();
            try {
                Files.delete(Paths.get(file));
            } catch (IOException ex) {
                LOGGER.warning(ex);
            }
        }
        for (Table t : tables) t.loadStatements();
    }

    PreparedStatement getStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    Statement getStatement() throws SQLException {
        return conn.createStatement();
    }

    String getSql(String name) {
        try {
            Path p = Paths.get(DatabaseController.class.getResource(name).toURI());
            return new String(Files.readAllBytes(p));
        } catch (URISyntaxException | IOException e) {
            LOGGER.warning(e);
        }
        return "";
    }

    public TableSetFocus getTblSetFocus() {
        return tblSetFocus;
    }

    public TableSetForm getTblSetForm() {
        return tblSetForm;
    }

    public TableTraining getTblTraining() {
        return tblTraining;
    }

    public TableSets getTblSet() {
        return tblSet;
    }

    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public TableTrainingContent getTblTrainingContent() {
        return tblTrainingContent;
    }
}
