package ch.tiim.sco.gui.events.focus;

import ch.tiim.sco.database.model.SetFocus;

public class FocusDeleteEvent extends FocusEvent {
    public FocusDeleteEvent(SetFocus focus) {
        super(focus);
    }
}
