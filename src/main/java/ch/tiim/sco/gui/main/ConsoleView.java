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
    private String lastCommand;

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
            String s = in.getText().trim();
            in.setText("");
            out.appendText("\n> " + s);
            try {
                eval(s);
            } catch (ScriptException e) {
                out.appendText("\n" + e.getMessage());
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                out.appendText("\nException:\n" + e.getMessage() + "\n" + sw.toString());
            }
            lastCommand = s;
        } else if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            in.appendText("\n");
        } else if (event.getCode() == KeyCode.UP) {
            in.setText(lastCommand);
        }
    }

    private void eval(String s) throws ScriptException {
        if (s.startsWith("$")) {
            String[] split = s.split(" ", 2);
            handle(split[0].trim(), split.length == 2 ? split[1].trim() : null);
        } else {
            Object eval = engine.eval(s);
            if (eval != null) {
                out.appendText("\n" + eval.toString());
            }
        }
    }

    private void handle(String cmd, String arg) throws ScriptException {
        engine.put("__cmd__", cmd);
        engine.put("__arg__", arg);
        switch (cmd) {
            case "$q":
                eval("db.debugQuery(__arg__);");
                break;
            default:
                eval("\"Unknown command \" + __cmd__");
                break;
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
