package ch.tiim.sco.gui.util;

import javafx.scene.control.ListCell;
import javafx.util.Callback;

public class BaseCell<T> extends ListCell<T> {

    private final Callback<T, String> c;

    public BaseCell(Callback<T, String> c) {

        this.c = c;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(c.call(item));
        } else {
            setText("");
        }
    }
}
