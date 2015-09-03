package ch.tiim.sco.database.jdbc;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCSwimmer extends Table implements ch.tiim.sco.database.TableTeamMember {
    private static final Logger LOGGER = LogManager.getLogger(JDBCSwimmer.class.getName());

    private NamedParameterPreparedStatement add;
    private NamedParameterPreparedStatement delete;
    private NamedParameterPreparedStatement update;
    private NamedParameterPreparedStatement getBetween;
    private NamedParameterPreparedStatement getAll;

    public JDBCSwimmer(DatabaseController db) throws SQLException {
        super(db);
    }

    @Override
    protected void loadStatements() throws SQLException {
        add = db.getPrepStmt(getSql("add"));
        delete = db.getPrepStmt(getSql("delete"));
        update = db.getPrepStmt(getSql("update"));
        getBetween = db.getPrepStmt(getSql("get_between"));
        getAll = db.getPrepStmt(getSql("get_all"));
    }

    @Override
    public void addSwimmer(Swimmer m) throws SQLException {
        add.setString("first_name", m.getFirstName());
        add.setString("last_name", m.getLastName());
        add.setString("birthday", m.getBirthDay().toString());
        add.setString("address", m.getAddress());
        add.setString("phone_private", m.getPhonePrivate());
        add.setString("phone_work", m.getPhoneWork());
        add.setString("phone_mobile", m.getPhoneMobile());
        add.setString("email", m.getEmail());
        add.setString("license", m.getLicense());
        add.setBoolean("is_female", m.isFemale());
        add.setString("notes", m.getNotes());
        LOGGER.debug(MARKER_QUERRY, add);
        testUpdate(add);
        m.setId(getGenKey(add));
    }

    @Override
    public void deleteSwimmer(Swimmer m) throws SQLException {
        delete.setInt("id", m.getId());
        LOGGER.debug(MARKER_QUERRY, delete);
        testUpdate(delete);
    }

    @Override
    public void updateSwimmer(Swimmer m) throws SQLException {
        update.setString("first_name", m.getFirstName());
        update.setString("last_name", m.getLastName());
        update.setString("birthday", m.getBirthDay().toString());
        update.setString("address", m.getAddress());
        update.setString("phone_private", m.getPhonePrivate());
        update.setString("phone_work", m.getPhoneWork());
        update.setString("phone_mobile", m.getPhoneMobile());
        update.setString("email", m.getEmail());
        update.setString("license", m.getLicense());
        update.setBoolean("is_female", m.isFemale());
        update.setString("notes", m.getNotes());
        update.setInt("id", m.getId());
        LOGGER.debug(MARKER_QUERRY, update);
        testUpdate(update);
    }

    @Override
    public List<Swimmer> getSwimmersWithBirthdayBetween(LocalDate begin, LocalDate end) throws SQLException {
        getBetween.setString("before", begin.toString());
        getBetween.setString("after", end.toString());
        LOGGER.debug(MARKER_QUERRY, getBetween);
        ResultSet rs = getBetween.executeQuery();
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getSwimmer(rs));
        }
        return l;
    }

    @Override
    public List<Swimmer> getAllSwimmers() throws SQLException {
        ResultSet rs = getAll.executeQuery();
        LOGGER.debug(MARKER_QUERRY, getAll);
        List<Swimmer> l = new ArrayList<>();
        while (rs.next()) {
            l.add(getSwimmer(rs));
        }
        return l;
    }


    static Swimmer getSwimmer(ResultSet rs) throws SQLException {
        return new Swimmer(
                rs.getInt("swimmer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                LocalDate.parse(rs.getString("birthday")),
                rs.getString("address"),
                rs.getString("phone_private"),
                rs.getString("phone_work"),
                rs.getString("phone_mobile"),
                rs.getString("email"),
                rs.getString("license"),
                rs.getBoolean("is_female"),
                rs.getString("notes")
        );
    }
}
