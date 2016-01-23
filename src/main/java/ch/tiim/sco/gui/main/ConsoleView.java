package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ConsoleView extends MainView {

    @Inject(name = "db-controller")
    private DatabaseController db;

    @FXML
    private VBox root;
    @FXML
    private TextArea out;
    @FXML
    private TextArea in;

    private NashornScriptEngine engine;

    public ConsoleView() {
        engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("db", db);
        try {
            engine.eval(new InputStreamReader(ConsoleView.class.getResourceAsStream("/ch/tiim/sco/console.js")));
        } catch (ScriptException e) {
            LOGGER.error("Error during js engine setup", e);
        }
    }

    @FXML
    private void onClear() {
        out.setText("");
    }

    @FXML
    private void onTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            event.consume();
            String s = in.getText();
            in.setText("");
            out.appendText("\n> " + s);
            try {
                Object eval = engine.eval(s);
                if (eval != null) {
                    out.appendText("\n" + eval.toString());
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                out.appendText("\nException:\n" + e.getMessage() + "\n" + sw.toString());
            }
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
