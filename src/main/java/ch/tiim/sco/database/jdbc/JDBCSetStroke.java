package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.TableSetStroke;
import ch.tiim.sco.database.model.SetStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("HardCodedStringLiteral")
public class JDBCSetStroke extends Table implements TableSetStroke {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCSetStroke.class);
    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement getAll;

    public JDBCSetStroke(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        update = db.getPrepStmt(getSql("update"));
        delete = db.getPrepStmt(getSql("delete"));
        getAll = db.getPrepStmt(getSql("get_all"));
    }

    @Override
    public void addSetStroke(SetStroke stroke) throws SQLException {
        add.setString("name", stroke.getName());
        add.setString("abbr", stroke.getAbbr());
        add.setString("notes", stroke.getNotes());
        LOGGER.debug(MARKER_QUERY, add.toString());
        testUpdate(add);
        stroke.setId(getGenKey(add));
    }

    @Override
    public void updateSetStroke(SetStroke stroke) throws SQLException {
        update.setString("name", stroke.getName());
        update.setString("abbr", stroke.getAbbr());
        update.setString("notes", stroke.getNotes());
        update.setInt("id", stroke.getId());
        LOGGER.debug(MARKER_QUERY, update.toString());
        testUpdate(update);
    }

    @Override
    public void deleteSetStroke(SetStroke stroke) throws SQLException {
        delete.setInt("id", stroke.getId());
        LOGGER.debug(MARKER_QUERY, delete.toString());
        testUpdate(delete);
    }

    @Override
    public List<SetStroke> getAllStrokes() throws SQLException {
        ResultSet rs = getAll.executeQuery();
        LOGGER.debug(MARKER_QUERY, getAll.toString());
        List<SetStroke> l = new LinkedList<>();
        while (rs.next()) {
            l.add(getSetStroke(rs));
        }
        return l;
    }

    static SetStroke getSetStroke(ResultSet rs) throws SQLException {
        return getSetStroke(rs, "");
    }

    static SetStroke getSetStroke(ResultSet rs, String prefix) throws SQLException {
        return new SetStroke(
                rs.getInt("stroke_id"),
                rs.getString(prefix + "name"),
                rs.getString(prefix + "abbr"),
                rs.getString(prefix + "notes")
        );
    }
}
