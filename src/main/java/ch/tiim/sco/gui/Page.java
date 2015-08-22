package ch.tiim.sco.gui;

import javafx.scene.control.TextField;

import java.io.InputStream;

public abstract class Page {
    public static boolean validateTextField(TextField t, String pattern) {
        if (!t.getText().matches(pattern)) {
            t.requestFocus();
            t.selectAll();
            return false;
        }
        return true;
    }

    public void opened() {
    }

    public abstract String getName();

    public InputStream getIcon() {
        return getClass().getResourceAsStream("icon.png");
    }
}