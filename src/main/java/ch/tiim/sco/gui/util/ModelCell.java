package ch.tiim.sco.gui.util;

import ch.tiim.sco.database.model.Model;
import javafx.scene.control.ListCell;

public class ModelCell<T extends Model> extends ListCell<T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.uiString());
        } else {
            setText("");
        }
    }
}
