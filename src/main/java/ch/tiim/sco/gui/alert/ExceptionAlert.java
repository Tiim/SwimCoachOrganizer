package ch.tiim.sco.gui.alert;

import ch.tiim.sco.event.ShowDocumentEvent;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class ExceptionAlert extends Alert {
    public static final ButtonType BUTTON_REPORT = new ButtonType("Report");

    private final EventBus eventBus;

    private ExceptionAlert(@Nonnull Logger logger, @Nonnull String message,
                           @Nullable Throwable t, @Nullable EventBus eventBus) {
        super(AlertType.ERROR);
        this.eventBus = eventBus;
        logger.error(String.format("[ALERT] - %s", message), t);
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
    }

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static void showError(Logger logger, String message, Throwable t, EventBus eventBus) {
        Platform.runLater(() -> {
            ExceptionAlert alert = new ExceptionAlert(logger, message, t, eventBus);
            alert.handle();
        });
    }


    public void handle() {
        Optional<ButtonType> buttonType = showAndWait();
        if (buttonType.isPresent() && buttonType.get() == BUTTON_REPORT) {
            eventBus.post(new ShowDocumentEvent("https://github.com/Tiim/SwimCoachOrganizer/issues"));
        }
    }
}
