package ch.tiim.sco.lenex;

import ch.tiim.sco.database.model.Result;
import ch.tiim.sco.database.model.Stroke;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.lenex.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResultsData {
    private List<Meet> meets;

    public ResultsData(Lenex l) throws LenexException {
        this.meets = l.meets.meets;
    }

    public List<Result> getResultsForSwimmer(Swimmer s) throws LenexException {
        List<Result> results = new ArrayList<>();
        for (Meet meet : meets) {
            for (Club c : meet.clubs.clubs) {
                for (Athlete a : c.athletes.athletes) {
                    if (!s.getFirstName().equalsIgnoreCase(a.firstname) ||
                            !s.getLastName().equalsIgnoreCase(a.lastname)) {
                        continue;
                    }

                    for (ch.tiim.sco.lenex.model.Result r : a.results.results) {
                        results.add(getResultFromLenexResult(meet, r));
                    }
                    return results;
                }
            }
        }
        return results;
    }

    private Result getResultFromLenexResult(Meet m, ch.tiim.sco.lenex.model.Result r) throws LenexException {
        Event e = getEventFromId(m, r.eventid);
        Session s = getSessionFromEventId(m, r.eventid);

        if (e == null || s == null) throw new LenexException("Invalid eventid for result" + r);

        String meet = m.name;
        LocalDate date = s.date;
        Duration swimTime = r.swimTime.asDuration();
        Duration reactionTime = null;
        if (r.reactionTime != null) {
            reactionTime = r.reactionTime.asDuration();
        }
        Stroke stroke = e.swimstyle.stroke.toStroke();
        int distance = e.swimstyle.distance;

        return new Result(meet, date, swimTime, reactionTime, stroke, distance);
    }

    private Session getSessionFromEventId(Meet m, int eventid) {
        for (Session s : m.sessions.sessions) {
            for (Event e : s.events.events) {
                if (e.eventid == eventid) {
                    return s;
                }
            }
        }
        return null;
    }

    private Event getEventFromId(Meet m, int eventid) {
        for (Session s : m.sessions.sessions) {
            for (Event e : s.events.events) {
                if (e.eventid == eventid) {
                    return e;
                }
            }
        }
        return null;
    }
}
