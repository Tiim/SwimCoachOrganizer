package ch.tiim.sco;

import ch.tiim.inject.Injector;
import ch.tiim.log.Log;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.gui.root.RootView;
import ch.tiim.sco.update.*;
import ch.tiim.sco.util.async.DaemonFactory;
import ch.tiim.sco.util.async.ExecutorEventListener;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class Main extends Application {
    private static final Log LOGGER = new Log(Main.class);

    private Stage mainStage;
    private EventBus eventBus = new EventBus("Main");
    @SuppressWarnings("FieldCanBeLocal")
    private ExecutorEventListener listener;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        listener = new ExecutorEventListener(new ScheduledThreadPoolExecutor(5, new DaemonFactory()));
        eventBus.register(listener);
        eventBus.register(this);
        DatabaseController db = new DatabaseController("file.db");
        mainStage = primaryStage;

        Injector.getInstance().addInjectable(db, "db-controller");
        Injector.getInstance().addInjectable(mainStage, "main-stage");
        Injector.getInstance().addInjectable(this, "app");
        Injector.getInstance().addInjectable(eventBus, "event-bus");

        mainStage.setTitle("Swim Coach Organizer " + VersionChecker.getCurrentVersion());
        initRootLayout();
        if (getParameters().getNamed().containsKey("version")) {
            VersionChecker.overrideCurrentVersion(new Version(
                    getParameters().getNamed().get("version")
            ));
        }
        eventBus.post(new VersionCheckTask(eventBus));
    }


    private void initRootLayout() {
        RootView view = new RootView();
        Parent rootLayout = view.getParent();

        Scene s = new Scene(rootLayout);
        mainStage.setScene(s);
        mainStage.show();
        mainStage.setMinWidth(mainStage.getWidth());
        mainStage.setMinHeight(mainStage.getHeight());
    }

    @Override
    public void stop() throws Exception {

    }

    @Subscribe
    public void askForUpdate(NewVersionEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "The new version " + VersionChecker.getRemoteVersion() + " is available.\n" +
                            "Your version is " + VersionChecker.getCurrentVersion() + ".\n" +
                            "Would you like to update?", ButtonType.YES, ButtonType.NO
            );
            alert.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> t = alert.showAndWait();
            if (t.get() == ButtonType.YES) {
                Alert update = new Alert(Alert.AlertType.INFORMATION, "Update is in progress.. The app will close soon.");
                update.initStyle(StageStyle.UNDECORATED);
                update.getButtonTypes().clear();
                update.show();
                eventBus.post(new UpdatePerformer());
            }
        });
    }

    @Subscribe
    public void handleDeadEvents(DeadEvent event) {
        LOGGER.warning("Dead event received: " + event.getEvent());
    }
}
