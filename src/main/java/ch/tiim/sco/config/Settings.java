package ch.tiim.sco.config;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

public class Settings {

    private static final String CONFIG = "settings.properties"; //NON-NLS
    public static final Settings INSTANCE = new Settings(Paths.get(CONFIG));

    private Properties config;
    private Path file;

    private Settings(Path file) {
        this.file = file;
        this.config = new Properties();
        try {
            config.load(Files.newInputStream(file));
        } catch (IOException e) {
            //Ignore
        }
    }

    public synchronized int getInt(String key, int def) {
        return Integer.parseInt(getString(key, String.valueOf(def)));
    }

    public synchronized String getString(String key, String def) {
        if (!config.containsKey(key)) {
            setString(key, def);
        }
        return config.getProperty(key);
    }

    public synchronized void setString(String key, String value) {
        config.setProperty(key, value);
        try (OutputStream os = Files.newOutputStream(file)) {
            config.store(os, "User settings"); //NON-NLS
        } catch (IOException e) {
            //ignore
        }
    }

    public synchronized void setInt(String key, int val) {
        setString(key, String.valueOf(val));
    }

    public synchronized boolean getBoolean(String key, boolean def) {
        return Boolean.parseBoolean(getString(key, String.valueOf(def)));
    }

    public synchronized void setBoolean(String key, boolean val) {
        setString(key, String.valueOf(val));
    }

    public synchronized Locale getLocale(String locale, Locale def) {
        return Locale.forLanguageTag(getString(locale, def.toLanguageTag()));
    }

    public synchronized void setLocale(String locale, Locale val) {
        setString(locale, val.toLanguageTag());
    }
}
