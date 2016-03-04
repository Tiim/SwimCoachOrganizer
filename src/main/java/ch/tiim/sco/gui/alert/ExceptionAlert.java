package ch.tiim.sco.gui.alert;

import ch.tiim.sco.gui.events.ErrorReportEvent;
import ch.tiim.sco.util.error.ErrorReport;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ExceptionAlert extends Alert {
    public static final ButtonType BUTTON_REPORT = new ButtonType("Report");
    private static EventBus eventBus;

    private Throwable throwable;

    private ExceptionAlert(@Nonnull Logger logger, @Nonnull String message,
                           @Nullable Throwable t) {
        super(AlertType.ERROR);
        this.throwable = t;
        getButtonTypes().setAll(BUTTON_REPORT, ButtonType.CLOSE);
        setTitle("Error");
        setHeaderText(String.format("%s: %s", message, t != null ? t.getMessage() : ""));
        if (t != null) {
            setContentText("If you think this is a bug, feel free to report it.");
            TextArea text = new TextArea(getStackTrace(t));
            text.setEditable(false);
            text.setWrapText(true);
            text.setMaxWidth(Double.MAX_VALUE);
            text.setMaxHeight(Double.MAX_VALUE);
            getDialogPane().setExpandableContent(text);
        }
        logger.info("[ALERT] - displaying...");
    }

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static void showError(Logger logger, String message, Throwable t) {
        logger.error(String.format("[ALERT] -> %s", message), t);
        Platform.runLater(() -> {
            ExceptionAlert alert = new ExceptionAlert(logger, message, t);
            alert.handle();
        });
    }


    public void handle() {
        Optional<ButtonType> buttonType = showAndWait();
        if (buttonType.isPresent() && buttonType.get() == BUTTON_REPORT) {
            ErrorReport report = new ErrorReport();
            Path logfile = Paths.get("logfile.log");
            Path traceFile = Paths.get("file.db.trace.db");
            if (Files.exists(logfile)) {
                report.addFile(logfile);
            }
            if (Files.exists(traceFile)) {
                report.addFile(traceFile);
            }
            report.setThrowable(throwable);
            report.generateReport();
            eventBus.post(new ErrorReportEvent.ErrorReportOpenEvent(report, null));
        }
    }

    public static void setEventBus(EventBus eventBus) {
        ExceptionAlert.eventBus = eventBus;
    }
}
