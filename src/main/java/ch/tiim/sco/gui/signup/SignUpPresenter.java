package ch.tiim.sco.gui.signup;

import ch.tiim.inject.Inject;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.gui.utils.ModelCell;
import ch.tiim.sco.lenex.InvitationData;
import ch.tiim.sco.lenex.model.AgeGroup;
import ch.tiim.sco.lenex.model.Event;
import ch.tiim.sco.lenex.model.Session;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SignUpPresenter {

    @FXML
    private TreeView<Model> treeEvents;
    @FXML
    private ListView<Swimmer> listIncluded;
    @FXML
    private ListView<Swimmer> listExcluded;
    @FXML
    private Parent root;

    @Inject(name = "db-controller")
    private DatabaseController db;

    private TreeItem<Model> rootItem;
    private Stage stage;
    private InvitationData data;

    @FXML
    private void initialize() {
        rootItem = new TreeItem<>();
        treeEvents.setShowRoot(false);
        treeEvents.setRoot(rootItem);
        Callback<ListView<Swimmer>, ListCell<Swimmer>> factory = param -> new ModelCell<>();
        listExcluded.setCellFactory(factory);
        listIncluded.setCellFactory(factory);
        treeEvents.setCellFactory(param -> new ModelTreeCell());
    }

    @Inject
    private void injected() {
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign up for meet");
    }

    public void showAndWait() {
        stage.show();
    }

    public void setData(InvitationData data) {
        this.data = data;
        for (Session s : data.getSessions()) {
            TreeItem<Model> sessionItem = new TreeItem<>(s);
            rootItem.getChildren().add(sessionItem);
            for (Event e : s.events.events) {
                TreeItem<Model> eventItem = new TreeItem<>(e);
                sessionItem.getChildren().add(eventItem);
                if (e.ageGroups != null) {
                    for (AgeGroup ag : e.ageGroups.ageGroups) {
                        eventItem.getChildren().add(new TreeItem<>(ag));
                    }
                }
            }
        }
    }

    @FXML
    private void onBtnAdd() {

    }

    @FXML
    private void onBtnDelete() {

    }

    public static class ModelTreeCell extends TreeCell<Model> {
        @Override
        protected void updateItem(Model item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.uiString());
            } else {
                setText("");
            }
        }
    }

}
