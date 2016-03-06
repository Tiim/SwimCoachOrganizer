package ch.tiim.sco.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final Config INSTANCE = new Config();
    private static final String CONFIG = "private.properties";  //NON-NLS
    private static final String ALTERNATE = "public.properties"; //NON-NLS
    private Properties config;

    private Config() {
        InputStream res = Config.class.getResourceAsStream(CONFIG);
        if (res == null) {
            res = Config.class.getResourceAsStream(ALTERNATE);
        }
        config = new Properties();
        try {
            config.load(res);
        } catch (IOException e) {
            //Can't log this.. this is called before loggers are initialized :(
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public String getString(String key) {
        return config.getProperty(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }
}
