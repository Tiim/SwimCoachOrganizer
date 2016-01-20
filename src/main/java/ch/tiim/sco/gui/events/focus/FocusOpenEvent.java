package ch.tiim.sco.gui.events.focus;

import ch.tiim.sco.database.model.SetFocus;

public class FocusOpenEvent extends FocusEvent {

    public FocusOpenEvent(SetFocus focus) {
        super(focus);
    }
}
