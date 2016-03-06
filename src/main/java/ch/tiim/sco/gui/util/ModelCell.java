package ch.tiim.sco.gui.util;

import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.scene.control.ListCell;

public class ModelCell<T extends Model> extends ListCell<T> {

    private final ResourceBundleEx lang;

    public ModelCell(ResourceBundleEx lang) {
        this.lang = lang;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.uiString(lang));
        } else {
            setText("");
        }
    }
}
