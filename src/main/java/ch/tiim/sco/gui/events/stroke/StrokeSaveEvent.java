package ch.tiim.sco.gui.events.stroke;

import ch.tiim.sco.database.model.SetStroke;

public class StrokeSaveEvent extends StrokeEvent {
    public StrokeSaveEvent(SetStroke stroke) {
        super(stroke);
    }
}
