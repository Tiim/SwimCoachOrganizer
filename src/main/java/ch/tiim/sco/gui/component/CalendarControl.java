package ch.tiim.sco.gui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
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
import java.util.function.Consumer;
import java.util.function.Function;

public class CalendarControl<T> extends BorderPane {
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
    private ObjectProperty<Function<LocalDate, List<CalendarEvent<T>>>> callback =
            new SimpleObjectProperty<>();

    private ObjectProperty<Consumer<T>> onEventCallback =
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
        callback.addListener(observable -> dateChanged());
    }

    public void reloadEvents() {
        dateChanged();
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
            if (i >= day && i < maxDays + day) {
                labels[i].setText(String.valueOf(i - day + 1));
                populateDay(first.plusDays(i - day), eventBoxes[i]);
            } else {
                labels[i].setText("");
                eventBoxes[i].getChildren().clear();
            }
        }
    }

    private void populateDay(LocalDate localDate, VBox eventBox) {
        if (callback.get() != null) {
            List<CalendarEvent<T>> events = callback.get().apply(localDate);
            events.forEach(it -> it.setOnMouseClicked(it2 -> onEventCallback.get().accept(it.getObject())));
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

    public ObjectProperty<Function<LocalDate, List<CalendarEvent<T>>>> callbackProperty() {
        return callback;
    }

    public Function<LocalDate, List<CalendarEvent<T>>> getCallback() {
        return callback.get();
    }

    public void setCallback(Function<LocalDate, List<CalendarEvent<T>>> callback) {
        this.callback.set(callback);
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public Consumer<T> getOnEventCallback() {
        return onEventCallback.get();
    }

    public ObjectProperty<Consumer<T>> onEventCallbackProperty() {
        return onEventCallback;
    }

    public void setOnEventCallback(Consumer<T> onEventCallback) {
        this.onEventCallback.set(onEventCallback);
    }

    public static class CalendarEvent<T> extends HBox {
        private Label time;
        private Label text;
        private ObjectProperty<LocalTime> localTime = new SimpleObjectProperty<>();
        private ObjectProperty<Color> color = new SimpleObjectProperty<>();
        private T object;

        public CalendarEvent(LocalTime time, String name, Color color, T object) {
            this.time = new Label(time.toString());
            this.text = new Label(name);
            this.color.addListener(observable1 ->
                    this.setBackground(new Background(
                            new BackgroundFill(this.color.get(), CornerRadii.EMPTY, Insets.EMPTY))));
            this.color.set(color);
            this.localTime.set(time);
            this.localTime.addListener(observable -> this.time.setText(localTime.get().toString()));
            this.time.getStyleClass().add("time");
            this.text.getStyleClass().add("name");
            getStyleClass().add("event");
            getChildren().addAll(this.time, this.text);
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

        public T getObject() {
            return object;
        }
    }
}
