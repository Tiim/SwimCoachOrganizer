package ch.tiim.sco.lenex;

import ch.tiim.sco.lenex.model.Event;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.lenex.model.Meet;
import ch.tiim.sco.lenex.model.Session;

import java.util.ArrayList;
import java.util.List;

public class InvitationData {
    private List<Meet> meets;

    public InvitationData(Lenex l) throws LenexException {
        this.meets = l.meets.meets;
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        for (Meet meet : meets) {
            for (Session session : meet.sessions.sessions) {
                events.addAll(session.events.events);
            }
        }
        return events;
    }
}
