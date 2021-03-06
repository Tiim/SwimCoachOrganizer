package ch.tiim.sco.gui;

import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ResourceBundle;

public class ViewLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewLoader.class);
    private ResourceBundle bundle;

    public ViewLoader(ResourceBundleEx bundle) {
        this.bundle = bundle;
    }

    /**
     * Loads a .fxml file and returns the controller that is associated with it.
     * The .fxml file must be in the same package as the class and must have the same name as the class
     *
     * @param clazz the class of the controller
     * @param <T>   the type of the controller
     * @return the controller, loaded from the .fxml
     */
    @Nonnull
    public <T extends View> T load(@Nonnull Class<T> clazz) {
        T c;
        try {
            FXMLLoader loader = new FXMLLoader(clazz.getResource(clazz.getSimpleName() + ".fxml")); //NON-NLS
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

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void load(Parent parent, String path) {
        FXMLLoader loader = new FXMLLoader(parent.getClass().getResource(path));
        loader.setRoot(parent);
        loader.setResources(bundle);
        loader.setController(parent);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Can't load " + path, e);
        }
    }
}
