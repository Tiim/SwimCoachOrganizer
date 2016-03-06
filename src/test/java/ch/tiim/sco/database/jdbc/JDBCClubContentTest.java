package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.database.model.Team;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCClubContentTest {

    private DatabaseController db;
    private Club c1;
    private Club c2;
    private Team t1;
    private Team t2;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
        c1 = new Club("Club1", null, null, null, null, null, 123);
        c2 = new Club("Club2", null, null, null, null, null, 123);
        t1 = new Team("Team1");
        t2 = new Team("Team2");
        db.getTblClub().addClub(c1);
        db.getTblClub().addClub(c2);
        db.getTblTeam().addTeam(t1);
        db.getTblTeam().addTeam(t2);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testAddTeam() throws Exception {
        db.getTblClubContent().addTeam(c1, t1);
        db.getTblClubContent().addTeam(c1, t2);
        Assert.assertEquals(2, db.getTblClubContent().getTeams(c1).size());
        Assert.assertEquals(0, db.getTblClubContent().getTeams(c2).size());
    }

    @Test
    public void testDeleteTeam() throws Exception {
        db.getTblClubContent().addTeam(c1, t1);
        db.getTblClubContent().addTeam(c1, t2);
        db.getTblClubContent().deleteTeam(c1, t1);
        Assert.assertEquals(1, db.getTblClubContent().getTeams(c1).size());
        Assert.assertEquals(t2, db.getTblClubContent().getTeams(c1).get(0));
    }

    @Test
    public void testGetTeams() throws Exception {
        db.getTblClubContent().addTeam(c1, t1);
        db.getTblClubContent().addTeam(c1, t2);
        Assert.assertEquals(2, db.getTblClubContent().getTeams(c1).size());
        Assert.assertTrue("Club contains t1", db.getTblClubContent().getTeams(c1).contains(t1));
        Assert.assertTrue("Club contains t2", db.getTblClubContent().getTeams(c1).contains(t2));
    }

    @Test
    public void testGetNotTeams() throws Exception {
        db.getTblClubContent().addTeam(c1, t1);
        db.getTblClubContent().addTeam(c1, t2);
        Assert.assertEquals(2, db.getTblClubContent().getNotTeams(c2).size());
        Assert.assertTrue("Club2 does not contain t1", db.getTblClubContent().getNotTeams(c2).contains(t1));
        Assert.assertTrue("Club2 does not contain t2", db.getTblClubContent().getNotTeams(c2).contains(t2));
    }
}