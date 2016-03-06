package ch.tiim.sco.database.jdbc;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Course;
import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("HardCodedStringLiteral")
public class JDBCResultTest {

    private DatabaseController db;
    private Swimmer s;
    private Result r1;
    private Result r2;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseController(":memory:");
        s = new Swimmer("John", "Smith", LocalDate.of(2000, 1, 1), "Address", null, null, null, null, null, true, null);
        db.getTblSwimmer().addSwimmer(s);
        r1 = new Result("SCBB RZW", LocalDate.of(2015, 9, 3),
                Duration.ofMinutes(2), Duration.ofSeconds(1), Stroke.FREE, 200, Course.LCM);
        r2 = new Result("Oktobermeeting Allschwil",
                LocalDate.of(2001, 1, 20), Duration.ofSeconds(20),
                Duration.ofSeconds(1), Stroke.BUTTERFLY, 50, Course.SCM);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testAddResult() throws Exception {
        db.getTblResult().addResult(s, r1);
        db.getTblResult().addResult(s, r2);
        List<Result> r = db.getTblResult().getResults(s);
        assertThat(r, hasItems(r1, r2));
        assertThat(r.size(), is(2));
    }

    @Test
    public void testUpdateResult() throws Exception {
        db.getTblResult().addResult(s, r1);
        r1.setMeet("New Meet in Bern");
        db.getTblResult().updateResult(r1);
        assertThat(db.getTblResult().getResults(s).get(0), is(r1));
    }

    @Test
    public void testDeleteResult() throws Exception {
        db.getTblResult().addResult(s, r1);
        db.getTblResult().addResult(s, r2);
        db.getTblResult().deleteResult(r2);
        assertThat(db.getTblResult().getResults(s).size(), is(1));
        assertThat(db.getTblResult().getResults(s).get(0), is(r1));
    }
}