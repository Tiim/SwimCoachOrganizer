package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.database.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JDBCTeamContentTest {
    private static final Logger LOGGER = LogManager.getLogger(JDBCTeamContentTest.class.getName());
    private DatabaseController db;
    private Team team1;
    private Team team2;
    private Swimmer tm1;
    private Swimmer tm2;

    @Before
    public void setUp() throws Exception {
        team1 = new Team("Team 1");
        team2 = new Team("Team 2");
        tm1 = new Swimmer("Max", "Muster", LocalDate.of(1996, 1, 26), "Address1", "123456", null,
                null, "mmuster@mm.com", "lic1", false, "Is very dumb");
        tm2 = new Swimmer("Johanna", "Smith", LocalDate.of(1996, 2, 26), "Address2", "987654", null,
                null, "jsmith@js.com", "lic2", true, "Is very intelligent");
        db = new DatabaseController(":memory:");

        db.getTblTeam().addTeam(team1);
        db.getTblTeam().addTeam(team2);
        db.getTblTeamMember().addSwimmer(tm1);
        db.getTblTeamMember().addSwimmer(tm2);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testGetMembersForTeam() throws Exception {
        db.getTblTeamContent().addMember(team1, tm1);
        db.getTblTeamContent().addMember(team2, tm2);
        assertEquals(1, db.getTblTeamContent().getMembers(team1).size());
        assertEquals(tm1, db.getTblTeamContent().getMembers(team1).get(0));
        assertEquals(tm2, db.getTblTeamContent().getMembers(team2).get(0));
    }

    @Test
    public void testRemoveMemberFromTeam() throws Exception {
        db.getTblTeamContent().addMember(team1, tm1);
        db.getTblTeamContent().addMember(team1, tm2);
        db.getTblTeamContent().deleteMember(team1, tm2);
        assertEquals(1, db.getTblTeamContent().getMembers(team1).size());
        assertEquals(tm1, db.getTblTeamContent().getMembers(team1).get(0));
    }

    @Test
    public void testGetMembersNotInTeam() throws Exception {
        db.getTblTeamContent().addMember(team1, tm1);
        db.getTblTeamContent().addMember(team1, tm2);
        db.getTblTeamContent().deleteMember(team1, tm2);
        List<Swimmer> m = db.getTblTeamContent().getMembers(team1);
        List<Swimmer> nm = db.getTblTeamContent().getNotMembers(team1);
        assertEquals(1, nm.size());
        assertEquals(tm2, nm.get(0));
    }
}