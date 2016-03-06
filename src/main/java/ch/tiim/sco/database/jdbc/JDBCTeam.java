package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCTeam extends Table implements ch.tiim.sco.database.TableTeam {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCTeam.class);

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement getAll;


    public JDBCTeam(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        update = db.getPrepStmt(getSql("update"));
        getAll = db.getPrepStmt(getSql("get_all"));
    }

    @Override
    public void deleteTeam(Team t) throws SQLException {
        delete.setInt("id", t.getId());
        LOGGER.debug(MARKER_QUERY, delete.toString());
        testUpdate(delete);
    }

    @Override
    public void addTeam(Team t) throws SQLException {
        add.setString("name", t.getName());
        LOGGER.debug(MARKER_QUERY, add.toString());
        testUpdate(add);
        t.setId(getGenKey(add));
    }

    @Override
    public void updateTeam(Team t) throws SQLException {
        update.setString("name", t.getName());
        update.setInt("id", t.getId());
        LOGGER.debug(MARKER_QUERY, update.toString());
        testUpdate(update);
    }

    @Override
    public List<Team> getAllTeams() throws SQLException {
        ResultSet rs = getAll.executeQuery();
        LOGGER.debug(MARKER_QUERY, getAll.toString());
        List<Team> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getTeam(rs));
        }
        return l;
    }

    static Team getTeam(ResultSet rs) throws SQLException {
        return new Team(
                rs.getInt("team_id"),
                rs.getString("name")
        );
    }
}
