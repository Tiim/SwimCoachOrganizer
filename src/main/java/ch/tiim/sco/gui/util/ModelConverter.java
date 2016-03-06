package ch.tiim.sco.gui.util;

import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.util.StringConverter;

public class ModelConverter<T extends Model> extends StringConverter<T> {
    private final ResourceBundleEx lang;

    public ModelConverter(ResourceBundleEx lang) {
        this.lang = lang;
    }

    @Override
    public String toString(T object) {
        return object.uiString(lang);
    }

    @Override
    public T fromString(String string) {
        return null;
    }
}
