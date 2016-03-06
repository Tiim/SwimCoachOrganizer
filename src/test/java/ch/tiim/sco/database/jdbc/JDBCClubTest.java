package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Club;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCClubTest {

    private DatabaseController db;


    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testAddClub() throws Exception {
        Club c = club();
        db.getTblClub().addClub(c);
        assertEquals(c, db.getTblClub().getAll().get(0));
    }

    @Test
    public void testDeleteClub() throws Exception {
        Club c = club();
        db.getTblClub().addClub(c);
        db.getTblClub().addClub(club());
        db.getTblClub().deleteClub(c);
        assertEquals(1, db.getTblClub().getAll().size());
    }

    @Test
    public void testUpdateClub() throws Exception {
        Club c = club();
        db.getTblClub().addClub(c);
        c.setName("NEW NAME");
        db.getTblClub().updateClub(c);
        assertEquals(c, db.getTblClub().getAll().get(0));
    }

    @Test
    public void testGetAll() throws Exception {
        db.getTblClub().addClub(club());
        db.getTblClub().addClub(club());
        db.getTblClub().addClub(club());
        db.getTblClub().addClub(club());
        assertEquals(4, db.getTblClub().getAll().size());
    }

    private Club club() {
        return new Club(
                "Club_name",
                "Club_name_short",
                "Club_name_en",
                "Club_name_en_short",
                "code",
                "SUI",
                1234
        );
    }
}