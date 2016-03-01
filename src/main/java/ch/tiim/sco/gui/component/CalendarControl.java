package ch.tiim.sco.gui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class CalendarControl extends BorderPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarControl.class);
    private static final int DAYS_OF_WEEK = 7;
    private static final int WEEKS = 6;

    @FXML
    private Label title;
    @FXML
    private GridPane grid;

    private Label[] labels = new Label[WEEKS * DAYS_OF_WEEK];
    private VBox[] eventBoxes = new VBox[WEEKS * DAYS_OF_WEEK];
    private ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<Function<LocalDate, List<CalendarEvent>>> callback =
            new SimpleObjectProperty<>();

    public CalendarControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Can't load Calendar.fxml", e);
        }

        init();
        selectedDate.addListener(observable -> dateChanged());
    }

    private void init() {
        for (int i = 0; i < labels.length; i++) {
            BorderPane bp = new BorderPane();
            Label l = new Label();
            ScrollPane sp = new ScrollPane();
            VBox vb = new VBox();

            sp.setContent(vb);
            bp.setTop(l);
            bp.setCenter(sp);

            labels[i] = l;
            eventBoxes[i] = vb;

            grid.add(bp, i % DAYS_OF_WEEK, i / DAYS_OF_WEEK + 1);

            l.getStyleClass().add("day");
            sp.getStyleClass().add("scroll-pane-events");
            vb.getStyleClass().add("vbox-events");
            bp.getStyleClass().add("day-box");

            sp.setMinHeight(Double.MIN_VALUE);
        }
        dateChanged();
    }

    private void dateChanged() {
        LocalDate localDate = selectedDate.get();
        Month month = localDate.getMonth();
        title.setText(String.format("%s, %d",
                month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()),
                localDate.getYear()
        ));
        LocalDate first = localDate.withDayOfMonth(1);
        int day = first.getDayOfWeek().getValue() - 1;
        int maxDays = YearMonth.from(localDate).lengthOfMonth();
        for (int i = 0; i < DAYS_OF_WEEK * WEEKS; i++) {
            if (i > day && i < maxDays) {
                labels[i].setText(String.valueOf(i + 1));
                populateDay(first.plusDays(i), eventBoxes[i]);
            } else {
                labels[i].setText("");
                eventBoxes[i].getChildren().clear();
            }
        }
    }

    private void populateDay(LocalDate localDate, VBox eventBox) {
        if (callback.isNotNull().get()) {
            List<CalendarEvent> events = callback.get().apply(localDate);
            eventBox.getChildren().setAll(events);
        } else {
            eventBox.getChildren().clear();
        }
    }

    @FXML
    private void lastMonth() {
        selectedDate.setValue(selectedDate.get().minusMonths(1).withDayOfMonth(1));
    }

    @FXML
    private void nextMonth() {
        selectedDate.setValue(selectedDate.get().plusMonths(1).withDayOfMonth(1));
    }

    ///////////PROPERTIES///////////

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public ObjectProperty<Function<LocalDate, List<CalendarEvent>>> callbackProperty() {
        return callback;
    }

    public Function<LocalDate, List<CalendarEvent>> getCallback() {
        return callback.get();
    }

    public void setCallback(Function<LocalDate, List<CalendarEvent>> callback) {
        this.callback.set(callback);
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    //TODO: Change background to color
    public static class CalendarEvent extends HBox {
        private Label time;
        private Label text;
        private ObjectProperty<LocalTime> localTime = new SimpleObjectProperty<>();
        private ObjectProperty<Color> color = new SimpleObjectProperty<>();

        public CalendarEvent(LocalTime time, String name, Color color) {
            this.time = new Label(time.toString());
            this.text = new Label(name);
            this.color.set(color);
            localTime.set(time);
            localTime.addListener(observable -> this.time.setText(localTime.get().toString()));
            this.time.getStyleClass().add("time");
            this.text.getStyleClass().add("name");
            getStyleClass().add("event");
        }

        public ObjectProperty<LocalTime> timeProperty() {
            return localTime;
        }

        public StringProperty textProperty() {
            return text.textProperty();
        }

        public String getName() {
            return text.getText();
        }

        public LocalTime getTime() {
            return localTime.get();
        }

        public void setTime(LocalTime time) {
            this.localTime.set(time);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

    }
}
