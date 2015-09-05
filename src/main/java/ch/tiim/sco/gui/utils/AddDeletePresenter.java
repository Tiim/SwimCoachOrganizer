package ch.tiim.sco.gui.utils;

import ch.tiim.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AddDeletePresenter<T> {

    private static final Logger LOGGER = LogManager.getLogger(AddDeletePresenter.class.getName());
    @FXML
    private ListView<T> listExcluded;
    @FXML
    private ListView<T> listIncluded;
    @FXML
    private Parent root;
    private ListFactory<T> includedFactory;
    private ListFactory<T> excludedFactory;
    private Move<T> remove;
    private Move<T> add;
    private ObservableList<T> included = FXCollections.observableArrayList();
    private ObservableList<T> excluded = FXCollections.observableArrayList();
    private Stage stage;

    @FXML
    private void initialize() {
        listExcluded.setItems(excluded);
        listIncluded.setItems(included);
    }

    @Inject
    private void injected() {
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Select Set");
    }

    @FXML
    void onBtnAdd() {
        T m = listExcluded.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                add.move(m);
            } catch (Exception e) {
                LOGGER.warn("Error while adding", e);
            }
        }
        updateExcluded();
        updateIncluded();
    }

    private void updateExcluded() {
        try {
            excluded.setAll(excludedFactory.get());
        } catch (Exception e) {
            LOGGER.warn("Error while loading excluded", e);
        }
    }

    private void updateIncluded() {
        try {
            included.setAll(includedFactory.get());
        } catch (Exception e) {
            LOGGER.warn("Error while loading included", e);
        }
    }

    @FXML
    void onBtnOk() {
        stage.close();
    }

    @FXML
    void onBtnRemove() {
        T m = listIncluded.getSelectionModel().getSelectedItem();
        if (m != null) {
            try {
                remove.move(m);
            } catch (Exception e) {
                LOGGER.warn("Error while removing", e);
            }
        }
        updateExcluded();
        updateIncluded();
    }

    public void showAndWait() {
        updateExcluded();
        updateIncluded();
        stage.showAndWait();
    }

    public void setAdd(Move<T> add) {
        this.add = add;
    }

    public void setExcludedFactory(ListFactory<T> excludedFactory) {
        this.excludedFactory = excludedFactory;
    }

    public void setIncludedFactory(ListFactory<T> includedFactory) {
        this.includedFactory = includedFactory;
    }

    public void setRemove(Move<T> remove) {
        this.remove = remove;
    }

    public interface ListFactory<T> {
        List<T> get() throws Exception;
    }

    public interface Move<T> {
        void move(T t) throws Exception;
    }
}
