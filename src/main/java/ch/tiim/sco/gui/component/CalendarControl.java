package ch.tiim.sco.gui.component;

import ch.tiim.inject.Inject;
import ch.tiim.inject.Injector;
import ch.tiim.sco.gui.ViewLoader;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CalendarControl<T> extends BorderPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarControl.class);
    private static final PseudoClass NON_EMPTY = PseudoClass.getPseudoClass("non-empty"); //NON-NLS
    private static final PseudoClass IN_MONTH = PseudoClass.getPseudoClass("in-month"); //NON-NLS
    private static final int DAYS_OF_WEEK = 7;
    private static final int WEEKS = 6;
    private static final int DAYS = DAYS_OF_WEEK * WEEKS;

    @Inject(name = "lang")
    private ResourceBundleEx lang;

    @FXML
    private Label title;
    @FXML
    private GridPane grid;

    private BorderPane[] days = new BorderPane[DAYS];
    private Label[] labels = new Label[DAYS];
    private VBox[] eventBoxes = new VBox[DAYS];
    private PopOver popOver;

    private ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<Function<LocalDate, List<CalendarEvent<T>>>> callback =
            new SimpleObjectProperty<>();

    private ObjectProperty<BiConsumer<LocalDate, T>> onEventCallback =
            new SimpleObjectProperty<>();

    public CalendarControl() {
        ViewLoader.load(this, "Calendar.fxml"); //NON-NLS
        Injector.getInstance().inject(this, null);
        getStylesheets().add("ch/tiim/sco/gui/component/Calendar.css"); //NON-NLS
        init();
        selectedDate.addListener(observable -> dateChanged());
        callback.addListener(observable -> dateChanged());
    }

    public void reloadEvents() {
        dateChanged();
    }

    private void dateChanged() {
        LocalDate localDate = selectedDate.get();
        Month month = localDate.getMonth();
        title.setText(String.format(lang.str("gui.date.format"),
                month.getDisplayName(TextStyle.FULL, lang.getLocale()),
                localDate.getYear()
        ));
        LocalDate first = localDate.withDayOfMonth(1);
        int day = first.getDayOfWeek().getValue() - 1;
        int maxDays = YearMonth.from(localDate).lengthOfMonth();
        for (int i = 0; i < DAYS_OF_WEEK * WEEKS; i++) {
            if (i >= day && i < maxDays + day) {
                labels[i].setText(String.valueOf(i - day + 1));
                populateDay(first.plusDays(i - day), eventBoxes[i], days[i]);
                days[i].pseudoClassStateChanged(IN_MONTH, true);
            } else {
                labels[i].setText("");
                eventBoxes[i].getChildren().clear();
                days[i].pseudoClassStateChanged(IN_MONTH, false);
                days[i].pseudoClassStateChanged(NON_EMPTY, false);
            }
        }
    }

    private void populateDay(LocalDate localDate, VBox eventBox, BorderPane day) {
        if (callback.get() != null) {
            List<CalendarEvent<T>> events = callback.get().apply(localDate);
            events.forEach(it -> it.setOnMouseClicked(it2 -> onEventCallback.get().accept(localDate, it.getObject())));
            eventBox.getChildren().setAll(events);
            day.pseudoClassStateChanged(NON_EMPTY, !events.isEmpty());
        } else {
            eventBox.getChildren().clear();
            day.pseudoClassStateChanged(NON_EMPTY, false);
        }
    }

    private void init() {
        for (int i = 0; i < labels.length; i++) {
            BorderPane bp = new BorderPane();
            Label l = new Label();
            VBox vb = new VBox();

            bp.setTop(l);
            //bp.setCenter(sp);

            days[i] = bp;
            labels[i] = l;
            eventBoxes[i] = vb;


            grid.add(bp, i % DAYS_OF_WEEK, i / DAYS_OF_WEEK + 1);

            final int y = i;
            bp.setOnMouseClicked(event -> {
                if (!eventBoxes[y].getChildren().isEmpty()) {
                    popOver = new PopOver(vb);
                    popOver.getRoot().getStylesheets().addAll("ch/tiim/sco/gui/component/CalendarPopup.css"); //NON-NLS
                    popOver.show(bp);
                }
            });

            l.getStyleClass().add("day"); //NON-NLS
            vb.getStyleClass().add("vbox-events"); //NON-NLS
            bp.getStyleClass().add("day-box"); //NON-NLS
        }
        dateChanged();
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

    public ObjectProperty<BiConsumer<LocalDate, T>> onEventCallbackProperty() {
        return onEventCallback;
    }

    public Function<LocalDate, List<CalendarEvent<T>>> getCallback() {
        return callback.get();
    }

    public void setCallback(Function<LocalDate, List<CalendarEvent<T>>> callback) {
        this.callback.set(callback);
    }

    public BiConsumer<LocalDate, T> getOnEventCallback() {
        return onEventCallback.get();
    }

    public void setOnEventCallback(BiConsumer<LocalDate, T> onEventCallback) {
        this.onEventCallback.set(onEventCallback);
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public static class CalendarEvent<T> extends HBox {
        private final T object;
        private Label time;
        private Label text;
        private ObjectProperty<LocalTime> localTime = new SimpleObjectProperty<>();
        private ObjectProperty<Color> color = new SimpleObjectProperty<>();

        public CalendarEvent(LocalTime time, String name, Color color, T object) {
            this.time = new Label(time.toString());
            this.text = new Label(name);
            this.color.addListener(observable1 ->
                    this.setBackground(new Background(
                            new BackgroundFill(this.color.get(), CornerRadii.EMPTY, Insets.EMPTY))));
            this.color.set(color);
            this.localTime.set(time);
            this.localTime.addListener(observable -> this.time.setText(localTime.get().toString()));
            this.time.getStyleClass().add("time"); //NON-NLS
            this.text.getStyleClass().add("name"); //NON-NLS
            this.object = object;
            getStyleClass().add("event"); //NON-NLS
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

        public T getObject() {
            return object;
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
