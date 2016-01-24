package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCClubContent extends Table implements ch.tiim.sco.database.TableClubContent {
    private static final Logger LOGGER = LogManager.getLogger(JDBCClubContent.class.getName());
    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement get;
    private NamedParameterPreparedStatement getNot;
    private NamedParameterPreparedStatement deleteAll;

    public JDBCClubContent(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        get = db.getPrepStmt(getSql("get"));
        getNot = db.getPrepStmt(getSql("get_not"));
        deleteAll = db.getPrepStmt(getSql("delete_all"));
    }

    @Override
    public void addTeam(Club c, Team t) throws SQLException {
        add.setInt("club_id", c.getId());
        add.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, add);
        testUpdate(add);
    }

    @Override
    public void deleteTeam(Club c, Team t) throws SQLException {
        delete.setInt("club_id", c.getId());
        delete.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, delete);
        testUpdate(delete);
    }

    @Override
    public List<Team> getTeams(Club c) throws SQLException {
        get.setInt("club_id", c.getId());
        LOGGER.debug(MARKER_QUERRY, get);
        ResultSet rs = get.executeQuery();
        List<Team> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCTeam.getTeam(rs));
        }
        return l;
    }

    @Override
    public List<Team> getNotTeams(Club c) throws SQLException {
        getNot.setInt("club_id", c.getId());
        LOGGER.debug(MARKER_QUERRY, getNot);
        ResultSet rs = getNot.executeQuery();
        List<Team> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCTeam.getTeam(rs));
        }
        return l;
    }

    @Override
    public void setTeams(Club club, List<Team> teams) throws SQLException {
        deleteAll.setInt("club_id", club.getId());
        LOGGER.debug(MARKER_QUERRY, deleteAll);
        deleteAll.executeUpdate(); //Might affect zero rows
        if (!teams.isEmpty()) {
            for (Team t : teams) {
                add.setInt("club_id", club.getId());
                add.setInt("team_id", t.getId());
                add.addBatch();
            }
            testBatchUpdate(add);
        }
    }
}
