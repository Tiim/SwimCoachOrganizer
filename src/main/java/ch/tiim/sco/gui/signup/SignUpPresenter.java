package ch.tiim.sco.gui.signup;

import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.lenex.model.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class SignUpPresenter {

    @FXML
    private TreeView<Event> treeEvents;

    @FXML
    private ListView<Swimmer> listIncluded;

    @FXML
    private ListView<Swimmer> listExcluded;

    private TreeItem<Event> rootItem;

    private ObservableList<Event> events;

    @FXML
    private void initialize() {
        rootItem = new TreeItem<>();
        treeEvents.setShowRoot(false);
    }

    @FXML
    private void onBtnAdd() {

    }

    @FXML
    private void onBtnDelete() {

    }


}
