package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ConsoleView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private VBox root;
    @FXML
    private TextArea out;
    @FXML
    private TextArea in;

    private ScriptEngine engine;

    public ConsoleView() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("db", db);
    }

    @FXML
    private void onClear() {

    }

    @FXML
    private void onTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            String s = in.getText();
            in.setText("");
            out.appendText("\n> " + s);
            try {
                Object eval = engine.eval(s);
                out.appendText("\n" + eval.toString());
            } catch (ScriptException e) {
                LOGGER.warn("Script exception", e);
            }
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
