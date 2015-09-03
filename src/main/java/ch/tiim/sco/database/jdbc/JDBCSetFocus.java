package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.SetFocus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JDBCSetFocus extends Table implements ch.tiim.sco.database.TableSetFocus {
    private static final Logger LOGGER = LogManager.getLogger(JDBCSetFocus.class.getName());
    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement getAll;

    public JDBCSetFocus(DatabaseController db) throws SQLException {
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
    public void addSetFocus(SetFocus focus) throws SQLException {
        add.setString("name", focus.getName());
        add.setString("abbr", focus.getAbbr());
        add.setString("notes", focus.getNotes());
        LOGGER.debug(MARKER_QUERRY, add);
        testUpdate(add);
        focus.setId(getGenKey(add));
    }

    @Override
    public void updateSetFocus(SetFocus focus) throws SQLException {
        update.setString("name", focus.getName());
        update.setString("abbr", focus.getAbbr());
        update.setString("notes", focus.getNotes());
        update.setInt("id", focus.getId());
        LOGGER.debug(MARKER_QUERRY, update);
        testUpdate(update);
    }

    @Override
    public void deleteSetFocus(SetFocus focus) throws SQLException {
        delete.setInt("id", focus.getId());
        LOGGER.debug(MARKER_QUERRY, delete);
        testUpdate(delete);
    }

    @Override
    public List<SetFocus> getAllFoci() throws SQLException {
        ResultSet rs = getAll.executeQuery();
        LOGGER.debug(MARKER_QUERRY, getAll);
        List<SetFocus> l = new LinkedList<>();
        while (rs.next()) {
            l.add(getSetFocus(rs));
        }
        return l;
    }

    static SetFocus getSetFocus(ResultSet rs) throws SQLException {
        return getSetFocus(rs, "");
    }

    static SetFocus getSetFocus(ResultSet rs, String prefix) throws SQLException {
        return new SetFocus(
                rs.getInt("focus_id"),
                rs.getString(prefix + "name"),
                rs.getString(prefix + "abbr"),
                rs.getString(prefix + "notes")
        );
    }
}
