package ch.tiim.sco.gui;

import ch.tiim.sco.util.OutOfCoffeeException;
import javafx.fxml.FXMLLoader;

import javax.annotation.Nonnull;
import java.util.ResourceBundle;

public class ViewLoader {

    private static ResourceBundle bundle;

    /**
     * Loads a .fxml file and returns the controller that is associated with it.
     * The .fxml file must be in the same package as the class and must have the same name as the class
     *
     * @param clazz the class of the controller
     * @param <T>   the type of the controller
     * @return the controller, loaded from the .fxml
     */
    @Nonnull
    public static <T extends View> T load(@Nonnull Class<T> clazz) {
        T c;
        try {
            FXMLLoader loader = new FXMLLoader(clazz.getResource(clazz.getSimpleName() + ".fxml"));
            loader.setResources(bundle);
            loader.load();
            c = loader.getController();
            if (c == null) {
                throw new OutOfCoffeeException("No controller for " + clazz.getSimpleName() + ".fxml");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't load fxml: " + clazz.getSimpleName() + ".fxml", e);
        }
        return c;
    }

    public static void setBundle(ResourceBundle bundle) {
        ViewLoader.bundle = bundle;
    }
}
