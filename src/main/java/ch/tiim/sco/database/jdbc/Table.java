package ch.tiim.sco.database.jdbc;


import ch.tiim.sco.database.DatabaseController;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class Table {

    static final Marker MARKER_SQL = MarkerManager.getMarker("SQL");
    static final Marker MARKER_QUERRY = MarkerManager.getMarker("SQL_QUERY").addParents(MARKER_SQL);
    static final Marker MARKER_RESULT = MarkerManager.getMarker("SQL_RESULT").addParents(MARKER_SQL);

    protected final DatabaseController db;

    protected Table(DatabaseController db) throws SQLException {
        this.db = db;
        loadStatements();
    }

    protected abstract void loadStatements() throws SQLException;

    protected String getSql(String name) {
        String module = getClass().getSimpleName().replace("JDBC", "");
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
        for(int i : results) {
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
