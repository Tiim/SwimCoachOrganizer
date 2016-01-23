package ch.tiim.sco.gui;

import javafx.fxml.FXMLLoader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ViewLoader {
    private static final Logger LOGGER = LogManager.getLogger(ViewLoader.class);

    public static <T extends View> T load(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("View class is null");
        }
        FXMLLoader loader = null;
        T c = null;
        try {
            loader = new FXMLLoader(clazz.getResource(clazz.getSimpleName() + ".fxml"));
            loader.load();
            c = loader.getController();
            if (c == null) {
                LOGGER.warn("No controller for " + clazz.getSimpleName() + ".fxml");
            }
        } catch (Exception e) {
            LOGGER.error("Can't load fxml: " + clazz.getSimpleName() + ".fxml", e);
            return null;
        }
        return c;
    }
}
