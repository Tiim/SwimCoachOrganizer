package ch.tiim.sco.database;

import ch.tiim.sco.database.jooq.tables.records.ClubRecord;
import ch.tiim.sco.database.model.Club;

import java.util.List;

import static ch.tiim.sco.database.jooq.Tables.CLUB;

public class TableClub extends Table {

    public TableClub(DatabaseController db) {
        super(db);
    }

    public void addClub(Club c) {
        ClubRecord f = db.getDsl().newRecord(CLUB, c);
        f.store();
    }

    public void deleteClub(Club c) {
        db.getDsl().delete(CLUB).where(CLUB.CLUB_ID.equal(c.getId())).execute();
    }

    public void updateClub(Club c) {
        ClubRecord f = db.getDsl().newRecord(CLUB, c);
        f.update();
    }

    public List<Club> getAll() {
        return db.getDsl().select()
                .from(CLUB)
                .fetchInto(Club.class);
    }
}
