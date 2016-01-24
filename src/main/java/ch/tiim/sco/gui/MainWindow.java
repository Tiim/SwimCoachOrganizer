package ch.tiim.sco.gui;

import ch.tiim.inject.Inject;
import ch.tiim.sco.config.Config;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.main.*;
import ch.tiim.sco.gui.util.DialogListener;
import ch.tiim.sco.update.Version;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindow extends View {

    private DialogView[] dialogViews;

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
    private void initialize() {
        initDialogs();
        initToolbar();

        Scene mainScene = new Scene(root);
        mainStage.setTitle(config.getString("config.window.name") + " " + version);
        mainStage.setScene(mainScene);
        mainStage.setMinWidth(config.getInt("config.window.x"));
        mainStage.setMinHeight(config.getInt("config.window.y"));
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

        if (!version.isDeployed()) {
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
                    v.opened();
                });
            }
        }
    }

    public void show() {
        mainStage.show();
    }


    @Override
    public Parent getRoot() {
        return null;
    }
}
