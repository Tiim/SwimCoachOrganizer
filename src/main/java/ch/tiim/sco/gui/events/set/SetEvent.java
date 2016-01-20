package ch.tiim.sco.gui.events.set;

import ch.tiim.sco.database.model.Set;

public abstract class SetEvent {

    private final Set set;

    public SetEvent(Set set) {
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

}
