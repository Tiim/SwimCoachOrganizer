package ch.tiim.sco.gui.events.set;

import ch.tiim.sco.database.model.Set;

public class SetDeleteEvent extends SetEvent {

    public SetDeleteEvent(Set set) {
        super(set);
    }
}
