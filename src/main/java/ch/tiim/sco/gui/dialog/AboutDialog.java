package ch.tiim.sco.gui.dialog;

import ch.tiim.inject.Inject;
import ch.tiim.sco.Main;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.TextOpenEvent;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AboutDialog extends DialogView {

    @Inject(name = "app")
    private Main application;

    @FXML
    private BorderPane root;
    @FXML
    private VBox pane;

    @FXML
    private void initialize() {
        List<Credit> credits = new ArrayList<>();
        credits.add(new Credit("flaticon.jpeg", "Icons are from www.flaticon.com", () -> openDocument("icons.md")));
        credits.add(new Credit("h2.png", "H2 Database", "http://www.h2database.com"));
        credits.add(new Credit("javafx.png", "JavaFX GUI Library", "http://www.javafx.com/"));
        credits.add(new Credit("log4j.jpg", "Logging framework: Log4J", "https://logging.apache.org/log4j/2.x/"));
        credits.add(new Credit("apache-fop.jpg", "PDF Generation", "https://xmlgraphics.apache.org/fop/"));
        initCredits(credits);
        root.setMinHeight(Region.USE_COMPUTED_SIZE);
        root.setMinWidth(Region.USE_COMPUTED_SIZE);
    }

    private void openDocument(String s) {
        String str;
        try {
            str = Resources.toString(AboutDialog.class.getResource("about/" + s), Charsets.UTF_8);
        } catch (IOException e) {
            new ExceptionAlert(LOGGER, "Can't read file from classpath", e, eventBus).handle();
            return;
        }
        eventBus.post(new TextOpenEvent(str, getStage()));
    }

    private void initCredits(List<Credit> credits) {
        for (Credit c : credits) {
            ImageView v = new ImageView(getImage(c.getImage(), 100, Integer.MAX_VALUE));
            HBox hBox = new HBox(v, new Label(c.getText()));
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            if (c.getCallback() == null) {
                hBox.setOnMouseClicked(event -> openLink(c.getUrl()));
            } else {
                hBox.setOnMouseClicked(event -> c.getCallback().run());
            }
            pane.getChildren().add(hBox);
        }
    }

    private Image getImage(String path, int maxWidth, int maxHeight) {
        return new Image(AboutDialog.class.getResource("about/" + path).toString(),
                maxWidth, maxHeight, true, true, true);
    }

    private void openLink(String url) {
        application.getHostServices().showDocument(url);
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    private static class Credit {
        private final String image;
        private final String text;
        private final String url;
        private final Runnable callback;

        public Credit(String image, String text, String url) {
            this.image = image;
            this.text = text;
            this.url = url;
            callback = null;
        }

        public Credit(String image, String text, Runnable callback) {
            this.image = image;
            this.text = text;
            this.callback = callback;
            url = null;
        }

        public Runnable getCallback() {
            return callback;
        }

        public String getImage() {
            return image;
        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}
