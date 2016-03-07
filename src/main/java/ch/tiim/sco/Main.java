package ch.tiim.sco;

import ch.tiim.inject.Inject;
import ch.tiim.inject.Injector;
import ch.tiim.sco.config.Config;
import ch.tiim.sco.config.Settings;
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
import ch.tiim.sco.util.lang.ResourceBundleEx;
import ch.tiim.sco.util.lang.ResourceBundleUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class Main extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final EventBus eventBus = new EventBus("Main"); //NON-NLS

    private ResourceBundleEx bundle;
    private ViewLoader viewLoader;
    private Settings settings;


    public static void main(final String[] args) {
        LOGGER.trace("Starting up"); //NON-NLS
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ExecutorEventListener listener = new ExecutorEventListener(new ScheduledThreadPoolExecutor(5, new DaemonFactory()));

        eventBus.register(listener);
        eventBus.register(this);

        settings = new Settings(Paths.get("settings.properties")); //NON-NLS
        Locale locale = settings.getLocale("default_locale", Locale.getDefault());
        bundle = new ResourceBundleEx(ResourceBundleUtil.getResourceBundle(locale));
        viewLoader = new ViewLoader(bundle);

        ExceptionAlert.init(eventBus, bundle);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {
                ExceptionAlert.showError(LOGGER,
                        bundle.str("error.bug.exception"),
                        e);
            } catch (Throwable ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw ex;
            }
        });

        DatabaseController db = new DatabaseController("./file.db"); //NON-NLS
        db.initializeDefaultValues();

        Injector.getInstance().addInjectable(viewLoader, "view-loader");
        Injector.getInstance().addInjectable(bundle, "lang");
        Injector.getInstance().addInjectable(getHostServices(), "host"); //NON-NLS
        Injector.getInstance().addInjectable(new Config(), "config"); //NON-NLS
        Injector.getInstance().addInjectable(db, "db-controller"); //NON-NLS
        Injector.getInstance().addInjectable(primaryStage, "main-stage"); //NON-NLS
        Injector.getInstance().addInjectable(this, "app"); //NON-NLS
        Injector.getInstance().addInjectable(eventBus, "event-bus"); //NON-NLS
        Injector.getInstance().addInjectable(VersionChecker.getCurrentVersion(), "version"); //NON-NLS

        initRootLayout();
        if (getParameters().getNamed().containsKey("version")) { //NON-NLS
            VersionChecker.overrideCurrentVersion(Version.valueOf(
                    getParameters().getNamed().get("version") //NON-NLS
            ));
        }
        eventBus.post(new VersionCheckTask(eventBus));
    }


    private void initRootLayout() {
        MainWindow mainWindow = viewLoader.load(MainWindow.class);
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
                    String.format(bundle.str("text.update"), VersionChecker.getRemoteVersion(), VersionChecker.getCurrentVersion()),
                    ButtonType.YES, ButtonType.NO
            );
            alert.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> t = alert.showAndWait();
            if (t.get() == ButtonType.YES) {
                eventBus.post(new UpdatePerformer(eventBus, bundle));
            }
        });
    }

    @Subscribe
    public void handleDeadEvents(DeadEvent event) {
        LOGGER.warn("Dead event received: " + event.getEvent()); //NON-NLS
    }
}
