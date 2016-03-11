package ch.tiim.sco.gui;

import ch.tiim.inject.Inject;
import ch.tiim.sco.config.Config;
import ch.tiim.sco.config.Settings;
import ch.tiim.sco.gui.events.AboutEvent;
import ch.tiim.sco.gui.main.*;
import ch.tiim.sco.gui.util.DialogListener;
import ch.tiim.sco.util.Logging;
import ch.tiim.sco.util.OutOfCoffeeException;
import com.github.zafarkhaja.semver.Version;
import com.google.common.io.Resources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainWindow extends View {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

    @Inject(name = "config")
    private Config config;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "version")
    private Version version;
    @Inject(name ="view-loader")
    private ViewLoader viewLoader;

    @FXML
    private BorderPane root;
    @FXML
    private ToolBar toolBar;
    @FXML
    private MenuBar menu;

    private Menu lastMenu;

    @FXML
    private void initialize() throws IOException {
        initDialogs();
        initToolbar();

        Scene mainScene = new Scene(root);
        mainStage.setTitle(config.getString("window.name") + " " + version);
        mainStage.setScene(mainScene);
        mainStage.setMinWidth(config.getInt("window.x"));
        mainStage.setMinHeight(config.getInt("window.y"));

        //Wait until the stage got resized. Otherwise
        //the split plane has not the right ratio.
        Platform.runLater(() -> ((Button) toolBar.getItems().get(0)).getOnAction().handle(null));
    }

    private void initDialogs() {
        eventBus.register(new DialogListener(viewLoader));
    }

    private void initToolbar() {

        List<MainView> views = new ArrayList<>(Arrays.asList(
                viewLoader.load(TrainingView.class),
                viewLoader.load(SetView.class),
                null,
                viewLoader.load(FocusView.class),
                viewLoader.load(StrokeView.class),
                null,
                viewLoader.load(ScheduleView.class),
                viewLoader.load(ClubView.class),
                viewLoader.load(TeamView.class),
                viewLoader.load(SwimmerView.class),
                viewLoader.load(BirthdayView.class),
                null,
                viewLoader.load(ResultView.class)
        ));

        if (version.equals(Version.forIntegers(0))) {
            views.add(null);
            views.add(viewLoader.load(ConsoleView.class));
        }

        for (MainView v : views) {
            if (v == null) {
                toolBar.getItems().add(new Separator());
            } else {
                ImageView icon = new ImageView(new Image(v.getIcon(), 32, 32, true, true));
                Button b = new Button(null, icon);
                b.setTooltip(new Tooltip(v.getName()));
                toolBar.getItems().add(b);
                b.setOnAction(event -> {
                    root.setCenter(v.getRoot());
                    this.menu.getMenus().set(1, v.getMenu());
                    v.opened();
                });
            }
        }
    }

    @FXML
    private void onAbout() {
        eventBus.post(new AboutEvent(mainStage));
    }

    @FXML
    private void onClose() {
        mainStage.close();
    }

    public void show() {
        mainStage.show();
    }

    @Override
    public Parent getRoot() {
        return null;
    }
}
