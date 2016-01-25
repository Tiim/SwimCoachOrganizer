package ch.tiim.sco.config;

import jdk.nashorn.api.scripting.JSObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

public class Config {

    private static final String CONFIG = "config.js";

    private ScriptEngine engine;

    private JSObject config;

    public Config() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
        engine.eval(new InputStreamReader(Config.class.getResourceAsStream(CONFIG)));
        config = (JSObject) engine.get("config");
    }


    public String getString(String key) {
        return (String) config.eval(key);
    }

    public int getInt(String key) {
        return (Integer) config.eval(key);
    }
}
