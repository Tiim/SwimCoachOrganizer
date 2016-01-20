package ch.tiim.sco.gui.events.stroke;

import ch.tiim.sco.database.model.SetStroke;

public class StrokeDeleteEvent extends StrokeEvent {
    public StrokeDeleteEvent(SetStroke stroke) {
        super(stroke);
    }
}
