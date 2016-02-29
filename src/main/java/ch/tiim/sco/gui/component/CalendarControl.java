package ch.tiim.sco.gui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarControl extends BorderPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarControl.class);
    private static final int DAYS_OF_WEEK = 7;
    private static final int WEEKS = 6;

    @FXML
    private Label title;
    @FXML
    private GridPane grid;

    private Label[] labels = new Label[WEEKS * DAYS_OF_WEEK];
    private ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());

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
            Label l = new Label();
            labels[i] = l;
            grid.add(l, i % DAYS_OF_WEEK, i / DAYS_OF_WEEK + 1);
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
            } else {
                labels[i].setText("");
            }
        }
    }

    @FXML
    private void lastMonth() {
        selectedDate.setValue(selectedDate.get().minusMonths(1).withDayOfMonth(1));
    }

    @FXML
    private void nextMonth() {
        selectedDate.setValue(selectedDate.get().minusMonths(1).withDayOfMonth(1));
    }

    ///////////PROPERTIES///////////

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.set(selectedDate);
    }
}
