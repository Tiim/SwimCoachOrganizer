package ch.tiim.sco;

import ch.tiim.inject.Injector;
import ch.tiim.sco.config.Config;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.event.ShowDocumentEvent;
import ch.tiim.sco.gui.MainWindow;
import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.update.NewVersionEvent;
import ch.tiim.sco.update.UpdatePerformer;
import ch.tiim.sco.update.VersionCheckTask;
import ch.tiim.sco.update.VersionChecker;
import ch.tiim.sco.util.async.DaemonFactory;
import ch.tiim.sco.util.async.ExecutorEventListener;
import com.github.zafarkhaja.semver.Version;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    private final EventBus eventBus = new EventBus("Main");

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ExecutorEventListener listener = new ExecutorEventListener(new ScheduledThreadPoolExecutor(5, new DaemonFactory()));

        eventBus.register(listener);
        eventBus.register(this);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {
                ExceptionAlert.showError(LOGGER,
                        String.format("Uncaught exception in thread: %s.\nThis is a bug! Please report it.", t.getName()),
                        e, eventBus);
            } catch (Throwable ex) {
                LOGGER.error(ex, ex);
                throw ex;
            }
        });

        DatabaseController db = new DatabaseController("./file.db");
        db.initializeDefaultValues();

        Injector.getInstance().addInjectable(getHostServices(), "host");
        Injector.getInstance().addInjectable(new Config(), "config");
        Injector.getInstance().addInjectable(db, "db-controller");
        Injector.getInstance().addInjectable(primaryStage, "main-stage");
        Injector.getInstance().addInjectable(this, "app");
        Injector.getInstance().addInjectable(eventBus, "event-bus");
        Injector.getInstance().addInjectable(VersionChecker.getCurrentVersion(), "version");

        initRootLayout();
        if (getParameters().getNamed().containsKey("version")) {
            VersionChecker.overrideCurrentVersion(Version.valueOf(
                    getParameters().getNamed().get("version")
            ));
        }
        eventBus.post(new VersionCheckTask(eventBus));
    }


    private void initRootLayout() {
        MainWindow mainWindow = ViewLoader.load(MainWindow.class);
        mainWindow.show();
    }

    @Override
    public void stop() throws Exception {

    }

    @Subscribe
    public void onShowDocument(ShowDocumentEvent event) {
        getHostServices().showDocument(event.getDocument());
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
                eventBus.post(new UpdatePerformer(eventBus));
            }
        });
    }

    @Subscribe
    public void handleDeadEvents(DeadEvent event) {
        LOGGER.warn("Dead event received: " + event.getEvent());
    }
}
