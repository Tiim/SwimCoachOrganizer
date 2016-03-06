package ch.tiim.sco.database.jdbc;


import ch.tiim.sco.database.DatabaseController;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class Table {

    static final Marker MARKER_QUERY = MarkerFactory.getMarker("SQL_QUERY"); //NON-NLS

    protected final DatabaseController db;

    protected Table(DatabaseController db) throws SQLException {
        this.db = db;
        loadStatements();
    }

    protected abstract void loadStatements() throws SQLException;

    protected String getSql(String name) {
        String module = getClass().getSimpleName().replace("JDBC", ""); //NON-NLS
        return db.getSqlLoader().getValue(module, name);
    }

    protected void testUpdate(PreparedStatement stmt) throws SQLException {
        int rows = stmt.executeUpdate();
        if (rows == 0) {
            throw new SQLException("Update failed, no rows affected.");
        }
    }

    protected void testBatchUpdate(PreparedStatement stmt) throws SQLException {
        int[] results = stmt.executeBatch();
        for (int i : results) {
            if (i == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        }
    }

    protected int getGenKey(PreparedStatement stmt) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Update failed, no ID obtained.");
            }
        }
    }
}
