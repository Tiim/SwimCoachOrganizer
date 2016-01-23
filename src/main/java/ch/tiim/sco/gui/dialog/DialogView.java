package ch.tiim.sco.gui.dialog;

import ch.tiim.sco.gui.View;
import ch.tiim.sco.gui.events.OpenEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class DialogView extends View {

    private Stage stage;


    public void open(OpenEvent event, Stage parent) {
        if (stage == null) {
            Parent root = getRoot();
            if (root == null) {
                throw new NullPointerException("Root can't be null!");
            }
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle(getName());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
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
