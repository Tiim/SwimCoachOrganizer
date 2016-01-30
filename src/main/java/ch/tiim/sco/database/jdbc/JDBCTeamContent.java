package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCTeamContent extends Table implements ch.tiim.sco.database.TableTeamContent {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCTeamContent.class);

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement get;
    private NamedParameterPreparedStatement getNot;
    private NamedParameterPreparedStatement deleteAll;

    public JDBCTeamContent(DatabaseController db) throws SQLException {
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
    public List<Swimmer> getSwimmers(Team t) throws SQLException {
        get.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, get.toString());
        ResultSet rs = get.executeQuery();
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCSwimmer.getSwimmer(rs));
        }
        return l;
    }

    @Override
    public void addSwimmer(Team t, Swimmer m) throws SQLException {
        add.setInt("team_id", t.getId());
        add.setInt("swimmer_id", m.getId());
        LOGGER.debug(MARKER_QUERRY, add.toString());
        testUpdate(add);
    }

    @Override
    public void deleteSwimmer(Team t, Swimmer m) throws SQLException {
        delete.setInt("team_id", t.getId());
        delete.setInt("swimmer_id", m.getId());
        LOGGER.debug(MARKER_QUERRY, delete.toString());
        testUpdate(delete);
    }

    @Override
    public List<Swimmer> getSwimmersNotInTeam(Team t) throws SQLException {
        getNot.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, getNot.toString());
        ResultSet rs = getNot.executeQuery();
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(JDBCSwimmer.getSwimmer(rs));
        }
        return l;
    }

    @Override
    public void setSwimmers(Team t, List<Swimmer> swimmers) throws Exception {
        deleteAll.setInt("team_id", t.getId());
        LOGGER.debug(MARKER_QUERRY, deleteAll.toString());
        deleteAll.executeUpdate(); //Might affect zero rows
        if (!swimmers.isEmpty()) {
            for (Swimmer s : swimmers) {
                add.setInt("team_id", t.getId());
                add.setInt("swimmer_id", s.getId());
                add.addBatch();
            }
            testBatchUpdate(add);
        }
    }
}
