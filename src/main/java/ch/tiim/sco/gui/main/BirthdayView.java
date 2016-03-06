package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.alert.ExceptionAlert;
import ch.tiim.sco.gui.events.SwimmerEvent;
import ch.tiim.sco.gui.util.BaseCell;
import ch.tiim.sco.util.BirthdayUtil;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class BirthdayView extends MainView {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayView.class);
    @Inject(name = "db-controller")
    private DatabaseController db;
    @Inject
    private ResourceBundleEx lang;

    @FXML
    private Parent root;
    @FXML
    private ListView<Swimmer> swimmers;
    @FXML
    private Label name;
    @FXML
    private Label birthday;
    @FXML
    private ImageView image;

    @FXML
    private void initialize() {
        swimmers.setCellFactory(param -> new BaseCell<>(swimmer ->
                String.format(lang.str("gui.birthday.format"),
                        swimmer.getFirstName(),
                        swimmer.getLastName(),
                        BirthdayUtil.daysUntilBirthday(swimmer.getBirthDay()))
        ));
        swimmers.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selected(newValue));
        populate();
    }

    private void selected(Swimmer swimmer) {
        name.setText(String.format(lang.str("gui.name.format"), swimmer.getFirstName(), swimmer.getLastName()));
        int days = BirthdayUtil.daysUntilBirthday(swimmer.getBirthDay());
        if (days == 0) {
            birthday.setText(lang.str("gui.birthday.today"));
            image.setVisible(true);
        } else {
            birthday.setText(String.format(lang.str("gui.birthday.in_days"), days));
            image.setVisible(false);
        }
    }

    private void populate() {
        List<Swimmer> s;
        try {
            s = db.getTblSwimmer().getAllSwimmers();
        } catch (Exception e) {
            ExceptionAlert.showError(LOGGER,lang.format("error.load", "error.subj.swimmer"), e);
            return;
        }
        Collections.sort(s, (o1, o2) ->
                BirthdayUtil.daysUntilBirthday(o1.getBirthDay()) - BirthdayUtil.daysUntilBirthday(o2.getBirthDay()));
        swimmers.getItems().setAll(s);
    }

    @Subscribe
    public void onSwimmer(SwimmerEvent event) {
        populate();
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
