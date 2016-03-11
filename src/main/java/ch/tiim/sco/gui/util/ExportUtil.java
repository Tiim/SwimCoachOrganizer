package ch.tiim.sco.gui.util;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.export.ExportController;
import ch.tiim.sco.database.export.ExportException;
import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ExportUtil {

    private final DatabaseController db;

    public ExportUtil(DatabaseController db) {
        this.db = db;
    }

    public Path export(Model m, Stage stage, ResourceBundleEx lang) {
        ExportController exp = new ExportController(db);
        exp.addData(m);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-File", ".xml"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            try {
                exp.exportToFile(file.toPath());
            } catch (Exception e) {
                new Alert(Alert.AlertType.WARNING,
                        lang.format("error.save", "error.subj.file") + " " + e.getMessage(),
                        ButtonType.OK
                ).showAndWait();
            }
            return file.toPath();
        }
        return null;
    }

    public Path export(List<Model> models, Stage stage, ResourceBundleEx lang) {
        ExportController exp = new ExportController(db);
        models.forEach(exp::addData);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-File", ".xml"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            try {
                exp.exportToFile(file.toPath());
            } catch (ExportException e) {
                new Alert(Alert.AlertType.WARNING,
                        lang.format("error.save", "error.subj.file") + "\n" + e.getMessage(),
                        ButtonType.OK
                ).showAndWait();
            }
            return file.toPath();
        }
        return null;
    }
}