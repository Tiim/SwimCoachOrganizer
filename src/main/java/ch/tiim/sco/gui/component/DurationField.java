package ch.tiim.sco.gui.component;

import ch.tiim.inject.Inject;
import ch.tiim.inject.Injector;
import ch.tiim.sco.gui.ViewLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.Duration;

public class DurationField extends HBox {

    private static final String FORMAT = "%02d";

    @Inject(name = "view-loader")
    private ViewLoader viewLoader;

    @FXML
    private TextField hour;
    @FXML
    private TextField min;
    @FXML
    private TextField sec;
    @FXML
    private TextField hs;


    private final ObjectProperty<Duration> duration;

    public DurationField() {
        Injector.getInstance().inject(this, null);
        viewLoader.load(this, "DurationField.fxml"); //NON-NLS
        getStylesheets().add("ch/tiim/sco/gui/component/DurationField.css"); //NON-NLS
        duration = new SimpleObjectProperty<>(Duration.ZERO);
        duration.addListener(observable -> populate());
        populate();
        hour.focusedProperty().addListener(event -> onChange(hour));
        min.focusedProperty().addListener(event -> onChange(min));
        sec.focusedProperty().addListener(event -> onChange(sec));
        hs.focusedProperty().addListener(event -> onChange(hs));
        hour.textProperty().addListener(observable -> hour.setPrefColumnCount(hour.getText().length() ));
        min.textProperty().addListener(observable -> min.setPrefColumnCount(min.getText().length() ));
        sec.textProperty().addListener(observable -> sec.setPrefColumnCount(sec.getText().length() ));
        hs.textProperty().addListener(observable -> hs.setPrefColumnCount(hs.getText().length() ));
    }

    private void populate() {
        hour.setText(String.format(FORMAT, (int) duration.get().toHours()));
        min.setText(String.format(FORMAT, (int) duration.get().toMinutes() % 60));
        sec.setText(String.format(FORMAT, (int) duration.get().getSeconds() % 60));
        hs.setText(String.format(FORMAT, (int) duration.get().toMillis()/10 % 100));
    }

    private void onChange(TextField t) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(t.getText());
        } catch (NumberFormatException e) {
            t.setText("00");
            return;
        }
        readDuration();
    }

    private void readDuration() {
        int hour = Integer.parseInt(this.hour.getText());
        int min = Integer.parseInt(this.min.getText());
        int sec = Integer.parseInt(this.sec.getText());
        int ms = Integer.parseInt(this.hs.getText()) * 10;
        Duration d = Duration.ofMillis(ms + sec * 1000 + min * 1000 * 60 + hour * 1000 * 60 * 60);
        duration.setValue(d);
    }

    public Duration getDuration() {
        return duration.get();
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration.set(duration);
    }
}
