package ch.tiim.sco.database;

import ch.tiim.jdbc.namedparameters.NamedParameterPreparedStatement;
import ch.tiim.sco.database.jdbc.*;
import ch.tiim.sql_xml.SqlLoader;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController implements Closeable {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class.getName());
    private static final String VERSION = "1";

    private final TableSetFocus tblSetFocus;
    private final TableSetForm tblSetForm;
    private final TableTraining tblTraining;
    private final TableTrainingContent tblTrainingContent;
    private final TableTeam tblTeam;
    private final TableTeamContent tblTeamContent;
    private final TableTeamMember tblTeamMember;
    private final TableSets tblSet;
    private final TableClub tblClub;
    private final TableClubContent tblClubContent;

    private final SqlLoader sqlLoader;
    private final Connection conn;
    private final Path filePath;
    private boolean initialized = false;

    public DatabaseController(String file) throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Database driver not found", e);
            throw new UnsupportedOperationException("org.h2.Driver not found!", e);
        }

        boolean notExists;
        if (!file.equals(":memory:")) {
            filePath = Paths.get(file);
            notExists = !Files.exists(filePath);
        } else {
            file = "mem:";
            filePath = null;
            notExists = true;
        }

        conn = DriverManager.getConnection("jdbc:h2:" + file);

        if (notExists) {
            mkDatabase();
        } else {
            initialized = true;
        }

        sqlLoader = new SqlLoader("/ch/tiim/sco/database/queries.sql.xml");
        tblSetFocus = new JDBCSetFocus(this);
        tblSetForm = new JDBCSetForm(this);
        tblTraining = new JDBCTraining(this);
        tblSet = new JDBCSets(this);
        tblTrainingContent = new JDBCTrainingContent(this);
        tblTeamContent = new JDBCTeamContent(this);
        tblTeamMember = new JDBCTeamMember(this);
        tblTeam = new JDBCTeam(this);
        tblClub = new JDBCClub(this);
        tblClubContent = new JDBCClubContent(this);
    }

    private void mkDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        String[] cmds = getSql("make.sql").split(";");
        for (String cmd : cmds) {
            stmt.addBatch(cmd);
        }
        stmt.executeBatch();
    }

    private String getSql(String name) {
        try (InputStreamReader is = new InputStreamReader(
                DatabaseController.class.getResourceAsStream(name), Charsets.UTF_8)) {
            return CharStreams.toString(is);
        } catch (IOException e) {
            LOGGER.warn("Could not load file " + name, e);
        }
        return "";
    }

    public SqlLoader getSqlLoader() {
        return sqlLoader;
    }

    public NamedParameterPreparedStatement getPrepStmt(String query) throws SQLException {
        return NamedParameterPreparedStatement.createNamedParameterPreparedStatement(conn, query);
    }

    public void initializeDefaultValues() throws SQLException {
        if (!initialized) {
            initialized = true;
        } else {
            LOGGER.info("Default values already initialized.. aborting!");
            return;
        }
        Statement stmt = conn.createStatement();
        String[] cmds = getSql("init.sql").split(";");
        for (String cmd : cmds) {
            stmt.addBatch(cmd);
        }
        stmt.executeBatch();
    }

    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException e) {
            LOGGER.warn("Can't close connection", e);
        }
    }

    public Connection getConn() {
        return conn;
    }

    public TableClub getTblClub() {
        return tblClub;
    }

    public TableClubContent getTblClubContent() {
        return tblClubContent;
    }

    public TableSets getTblSet() {
        return tblSet;
    }

    public TableSetFocus getTblSetFocus() {
        return tblSetFocus;
    }

    public TableSetForm getTblSetForm() {
        return tblSetForm;
    }

    public TableTeam getTblTeam() {
        return tblTeam;
    }

    public TableTeamContent getTblTeamContent() {
        return tblTeamContent;
    }

    public TableTeamMember getTblTeamMember() {
        return tblTeamMember;
    }

    public TableTraining getTblTraining() {
        return tblTraining;
    }

    public TableTrainingContent getTblTrainingContent() {
        return tblTrainingContent;
    }
}
