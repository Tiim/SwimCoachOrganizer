package ch.tiim.sco.gui.dialog;

import ch.tiim.sco.gui.View;
import ch.tiim.sco.gui.events.OpenEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DialogView extends View {

    private static final Logger LOGGER = LoggerFactory.getLogger(DialogView.class);

    private Stage stage;


    public void open(OpenEvent event, Stage parent) {
        if (stage == null) {
            Parent root = getRoot();
            if (root == null) {
                throw new NullPointerException("Root can't be null!");
            }
            String css = getClass().getCanonicalName().replace('.', '/') + ".css"; //NON-NLS
            if (DialogView.class.getResource('/' + css) != null) {
                root.getStylesheets().add(css);
            }
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle(getName());
            stage.setScene(scene);
            if (parent != null) {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(parent);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setOnCloseRequest(e -> onClosePrivate());
        }
        stage.show();
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    private void onClosePrivate() {
        stage = null;
        eventBus.unregister(this);
        onClose();
    }

    protected void onClose() {
        //Nothing here
    }

    protected void close() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isOpen() {
        return stage != null;
    }
}
