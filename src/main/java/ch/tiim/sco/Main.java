package ch.tiim.sco;

import ch.tiim.inject.Injector;
import ch.tiim.sco.config.Settings;
import ch.tiim.sco.event.ShowDocumentEvent;
import ch.tiim.sco.event.ShutdownEvent;
import ch.tiim.sco.gui.MainWindow;
import ch.tiim.sco.gui.Splash;
import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.update.NewVersionEvent;
import ch.tiim.sco.update.UpdatePerformer;
import ch.tiim.sco.update.VersionCheckTask;
import ch.tiim.sco.update.VersionChecker;
import ch.tiim.sco.util.async.DaemonFactory;
import ch.tiim.sco.util.async.ExecutorEventListener;
import ch.tiim.sco.util.init.*;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.github.zafarkhaja.semver.Version;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        Splash splash = new Splash(primaryStage);
        primaryStage.setScene(new Scene(splash));
        primaryStage.show();

        ExecutorEventListener listener =
                new ExecutorEventListener(new ScheduledThreadPoolExecutor(5, new DaemonFactory()));

        eventBus.register(listener);
        eventBus.register(this);

        primaryStage.setOnCloseRequest(event -> {
            eventBus.post(new ShutdownEvent());
            System.exit(0);
        });

        if (getParameters().getNamed().containsKey("version")) { //NON-NLS
            VersionChecker.overrideCurrentVersion(Version.valueOf(
                    getParameters().getNamed().get("version") //NON-NLS
            ));
        }

        initProgram(primaryStage, splash);
    }

    private void initProgram(Stage primaryStage, Splash splash) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        LoadConfig loadConfig = new LoadConfig();
        LoadLocale loadLoc = new LoadLocale();
        LoadSettings loadSettings = new LoadSettings();
        LoadDatabase loadDatabase = new LoadDatabase();
        LoadVersion loadVersion = new LoadVersion();

        DoubleBinding progress = loadConfig.progressProperty()
                .add(loadLoc.progressProperty())
                .add(loadSettings.progressProperty())
                .add(loadDatabase.progressProperty())
                .add(loadVersion.progressProperty()).divide(5);

        splash.progressProperty().bind(progress);

        loadVersion.setOnSucceeded(it -> {
            Injector.getInstance().addInjectable(loadVersion.getValue(), "version"); //NON-NLS
            eventBus.post(new VersionCheckTask(eventBus));
        });

        loadConfig.setOnSucceeded(it -> {
            Injector.getInstance().addInjectable(loadConfig.getValue(), "config"); //NON-NLS
        });

        loadSettings.setOnSucceeded(it -> {
            settings = loadSettings.getValue();
            loadLoc.setLocale(settings.getLocale("default_locale", Locale.getDefault()));
            executor.submit(loadLoc);
        });

        loadDatabase.setOnSucceeded(it -> {
            Injector.getInstance().addInjectable(loadDatabase.getValue(), "db-controller"); //NON-NLS
        });

        loadLoc.setOnSucceeded(it -> {
            bundle = loadLoc.getValue();
            viewLoader = new ViewLoader(bundle);
            Injector.getInstance().addInjectable(bundle, "lang");
            ExceptionAlert.init(eventBus, bundle);
            Injector.getInstance().addInjectable(viewLoader, "view-loader");
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
            initRootLayout();
            primaryStage.centerOnScreen();
        });

        Injector.getInstance().addInjectable(getHostServices(), "host"); //NON-NLS
        Injector.getInstance().addInjectable(primaryStage, "main-stage"); //NON-NLS
        Injector.getInstance().addInjectable(this, "app"); //NON-NLS
        Injector.getInstance().addInjectable(eventBus, "event-bus"); //NON-NLS


        executor.submit(loadVersion);
        executor.submit(loadConfig);
        executor.submit(loadSettings);
        executor.submit(loadDatabase);
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
