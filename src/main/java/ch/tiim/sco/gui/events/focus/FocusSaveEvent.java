package ch.tiim.sco.gui.events.focus;

import ch.tiim.sco.database.model.SetFocus;

public class FocusSaveEvent extends FocusEvent {
    public FocusSaveEvent(SetFocus focus) {
        super(focus);
    }
}
