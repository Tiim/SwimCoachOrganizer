package ch.tiim.sco.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class UIException extends Exception {
    public UIException() {
        super();
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(Throwable cause) {
        super(cause);
    }


    public void showDialog(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, getMessage(), ButtonType.OK);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

}
