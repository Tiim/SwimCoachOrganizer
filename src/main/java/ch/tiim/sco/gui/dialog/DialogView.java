package ch.tiim.sco.gui.dialog;

import ch.tiim.sco.gui.View;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class DialogView extends View {

    private Stage stage;


    protected void open(Stage parent) {
        if (stage == null) {
            Scene scene = new Scene(getRoot());
            stage = new Stage();
            stage.setTitle(getName());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
        }
        stage.show();
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    protected void close() {
        stage.close();
    }
}
