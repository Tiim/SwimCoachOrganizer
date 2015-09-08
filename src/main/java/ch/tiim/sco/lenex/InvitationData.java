package ch.tiim.sco.lenex;

import ch.tiim.sco.lenex.model.Event;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.lenex.model.Meet;
import ch.tiim.sco.lenex.model.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvitationData {

    private final Lenex l;

    public InvitationData(Lenex l) throws LenexException {
        this.l = l;
    }

    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        for (Meet m : l.meets.meets) {
            sessions.addAll(m.sessions.sessions);
        }
        Collections.sort(sessions);
        return sessions;
    }

    public List<Event> getEvents(Session s) {
        List<Event> events = s.events.events;
        Collections.sort(events);
        return events;
    }
}
