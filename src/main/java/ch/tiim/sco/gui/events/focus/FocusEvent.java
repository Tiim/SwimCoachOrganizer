package ch.tiim.sco.gui.events.focus;

import ch.tiim.sco.database.model.SetFocus;

public abstract class FocusEvent {

    private SetFocus focus;


    public FocusEvent(SetFocus focus) {
        this.focus = focus;
    }

    public SetFocus getFocus() {
        return focus;
    }
}
