package ch.tiim.sco.gui.util;

import ch.tiim.sco.database.model.Model;
import javafx.util.StringConverter;

public class ModelConverter<T extends Model> extends StringConverter<T> {
    @Override
    public String toString(T object) {
        return object.uiString();
    }

    @Override
    public T fromString(String string) {
        return null;
    }
}
