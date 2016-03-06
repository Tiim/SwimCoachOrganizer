package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCSwimmerTest {

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
    public void testAddMember() throws Exception {
        Swimmer m = member();
        db.getTblSwimmer().addSwimmer(m);
        assertEquals(m, db.getTblSwimmer().getAllSwimmers().get(0));
    }

    @Test
    public void testDeleteMember() throws Exception {
        Swimmer m = member();
        db.getTblSwimmer().addSwimmer(m);
        db.getTblSwimmer().deleteSwimmer(m);
        assertEquals(0, db.getTblSwimmer().getAllSwimmers().size());
    }

    @Test
    public void testUpdateMember() throws Exception {
        Swimmer m = member();
        db.getTblSwimmer().addSwimmer(m);
        m.setFirstName("New First Name");
        db.getTblSwimmer().updateSwimmer(m);
        assertEquals(m, db.getTblSwimmer().getAllSwimmers().get(0));
    }

    @Test
    public void testGetMembersWithBirthdayBetween() throws Exception {
        Swimmer m = member();
        m.setBirthDay(LocalDate.of(1995, 1, 1));
        db.getTblSwimmer().addSwimmer(m);
        List<Swimmer> b1 = db.getTblSwimmer().
                getSwimmersWithBirthdayBetween(LocalDate.of(1994, 1, 1), LocalDate.of(1996, 1, 1));
        assertTrue(b1.contains(m));
    }

    @Test
    public void testGetMembersWithBirthdayBetweenBorderStart() throws Exception {
        Swimmer m = member();
        m.setBirthDay(LocalDate.of(1995, 1, 1));
        db.getTblSwimmer().addSwimmer(m);
        List<Swimmer> b1 = db.getTblSwimmer().
                getSwimmersWithBirthdayBetween(LocalDate.of(1995, 1, 1), LocalDate.of(1996, 1, 1));
        assertTrue(b1.contains(m));
    }

    @Test
    public void testGetMembersWithBirthdayBetweenBorderEnd() throws Exception {
        Swimmer m = member();
        m.setBirthDay(LocalDate.of(1995, 1, 1));
        db.getTblSwimmer().addSwimmer(m);
        List<Swimmer> b1 = db.getTblSwimmer().
                getSwimmersWithBirthdayBetween(LocalDate.of(1994, 1, 1), LocalDate.of(1995, 1, 1));
        assertTrue(b1.contains(m));
    }

    @Test
    public void testGetAllMembers() throws Exception {
        db.getTblSwimmer().addSwimmer(member());
        db.getTblSwimmer().addSwimmer(member());
        db.getTblSwimmer().addSwimmer(member());
        db.getTblSwimmer().addSwimmer(member());
        assertEquals(4, db.getTblSwimmer().getAllSwimmers().size());
    }

    private Swimmer member() {
        return new Swimmer(
                "FirstName",
                "LastName",
                LocalDate.of(1996, 1, 26),
                "Address",
                "123 234 345",
                null,
                null,
                "email@mail.com",
                null,
                false,
                "Notes"
        );
    }

}