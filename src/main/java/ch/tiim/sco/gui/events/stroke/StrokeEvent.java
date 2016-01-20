package ch.tiim.sco.gui.events.stroke;

import ch.tiim.sco.database.model.SetStroke;

public abstract class StrokeEvent {

    private SetStroke stroke;

    public StrokeEvent(SetStroke stroke) {
        this.stroke = stroke;
    }

    public SetStroke getStroke() {
        return stroke;
    }
}
