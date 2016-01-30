package ch.tiim.sco.gui;

import ch.tiim.inject.Inject;
import ch.tiim.sco.config.Config;
import ch.tiim.sco.gui.events.AboutEvent;
import ch.tiim.sco.gui.main.*;
import ch.tiim.sco.gui.util.DialogListener;
import com.github.zafarkhaja.semver.Version;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindow extends View {

    @Inject(name = "config")
    private Config config;
    @Inject(name = "main-stage")
    private Stage mainStage;
    @Inject(name = "version")
    private Version version;

    @FXML
    private BorderPane root;
    @FXML
    private ToolBar toolBar;
    @FXML
    private MenuBar menu;

    private Menu lastMenu;

    @FXML
    private void initialize() {
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
        eventBus.register(new DialogListener());
    }

    private void initToolbar() {

        List<MainView> views = new ArrayList<>(Arrays.asList(
                ViewLoader.load(TrainingView.class),
                ViewLoader.load(SetView.class),
                null,
                ViewLoader.load(FocusView.class),
                ViewLoader.load(StrokeView.class),
                null,
                ViewLoader.load(ClubView.class),
                ViewLoader.load(TeamView.class),
                ViewLoader.load(SwimmerView.class),
                ViewLoader.load(BirthdayView.class),
                null,
                ViewLoader.load(ResultView.class)
        ));

        if (version.equals(Version.forIntegers(0))) {
            views.add(null);
            views.add(ViewLoader.load(ConsoleView.class));
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
