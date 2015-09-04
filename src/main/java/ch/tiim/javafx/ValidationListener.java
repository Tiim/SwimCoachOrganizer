package ch.tiim.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Parent;


public class ValidationListener implements ChangeListener<String> {
    public static PseudoClass ERROR_CLASS = PseudoClass.getPseudoClass("error");

    private final String pattern;
    private final Parent parent;

    public ValidationListener(String pattern, Parent parent) {
        this.pattern = pattern;
        this.parent = parent;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue == null) {
            newValue = "";
        }
        parent.pseudoClassStateChanged(ERROR_CLASS, !newValue.matches(pattern));
    }
}
