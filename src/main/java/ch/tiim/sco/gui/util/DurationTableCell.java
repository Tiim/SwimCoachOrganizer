package ch.tiim.sco.gui.util;

import ch.tiim.sco.util.DurationFormatter;
import javafx.scene.control.TableCell;

import java.time.Duration;

public class DurationTableCell<T> extends TableCell<T, Duration> {
    @Override
    protected void updateItem(Duration item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
        } else {
            setText(DurationFormatter.format(item));
        }
    }
}
