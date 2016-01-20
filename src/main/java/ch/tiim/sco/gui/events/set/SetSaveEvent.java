package ch.tiim.sco.gui.events.set;

import ch.tiim.sco.database.model.Set;

public class SetSaveEvent extends SetEvent {

    public SetSaveEvent(Set set) {
        super(set);
    }
}
