package ch.tiim.sco.gui.root;

import ch.tiim.inject.Inject;
import ch.tiim.javafx.View;
import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.gui.Page;
import ch.tiim.sco.gui.about.AboutView;
import ch.tiim.sco.gui.birthday.BirthdayView;
import ch.tiim.sco.gui.club.ClubView;
import ch.tiim.sco.gui.lenex.LenexView;
import ch.tiim.sco.gui.member.MemberView;
import ch.tiim.sco.gui.metadata.FocusView;
import ch.tiim.sco.gui.metadata.FormView;
import ch.tiim.sco.gui.results.ResultsView;
import ch.tiim.sco.gui.sets.SetsView;
import ch.tiim.sco.gui.team.TeamView;
import ch.tiim.sco.gui.training.TrainingView;
import ch.tiim.sco.util.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RootPresenter {
    private static final Logger LOGGER = LogManager.getLogger(RootPresenter.class.getName());
    private static final FileChooser.ExtensionFilter SQLITE_EXT =
            new FileChooser.ExtensionFilter("SQLite file", "*.db");

    @FXML
    private ToolBar toolbar;
    @FXML
    private BorderPane root;

    @Inject(name = "main-stage")
    private Stage stage;
    @Inject(name = "db-controller")
    private DatabaseController db;

    private AboutView viewAbout;


    @FXML
    private void initialize() {
        List<View<? extends Page>> pages = getPages();
        for (final View<? extends Page> v : pages) {
            if (v == null) {
                toolbar.getItems().add(new Separator());
            } else {
                ImageView view = new ImageView(new Image(v.getController().getIcon(), 32, 32, true, true));
                final Button t = new Button(null, view);
                t.setTooltip(new Tooltip(v.getController().getName()));
                toolbar.getItems().add(t);
                t.setOnAction(event -> {
                    root.setCenter(v.getParent());
                    v.getController().opened();
                });
            }
        }
        toolbar.getItems().addAll(getSpacer(), getBtnAbout());
        root.setCenter(pages.get(0).getParent());
    }

    private List<View<? extends Page>> getPages() {
        List<View<? extends Page>> pages = new ArrayList<>();
        pages.addAll(Arrays.asList(
                new TrainingView(),
                new SetsView(),
                null,
                new FocusView(),
                new FormView(),
                null,
                new ClubView(),
                new TeamView(),
                new MemberView(),
                null,
                new ResultsView(),
                new BirthdayView(),
                null,
                new LenexView()
        ));
        return pages;
    }

    private Node getSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(Region.USE_PREF_SIZE);
        return spacer;
    }

    private Node getBtnAbout() {
        Button btnAbout = new Button(null,
                new ImageView(new Image(
                        RootPresenter.class.getResourceAsStream("about.png"), 32, 32, true, true
                ))
        );
        viewAbout = new AboutView();
        btnAbout.setOnAction(event -> viewAbout.getController().show());
        return btnAbout;
    }

    @FXML
    void onMenuAbout() {
        viewAbout.getController().show();
    }

    @FXML
    void onMenuClose() {
        System.exit(0);
    }

    @FXML
    void onMenuExportSets() {
        Path f = FileChooserUtil.saveFile(stage, SQLITE_EXT);
        //noinspection ConstantConditions
        if (f != null) {
            throw new RuntimeException("NotImplemented");
        }
    }

    @FXML
    void onMenuExportMembers() {
        Path f = FileChooserUtil.saveFile(stage, SQLITE_EXT);
        //noinspection ConstantConditions
        if (f != null) {
            throw new RuntimeException("NotImplemented");
        }
    }

    @FXML
    void onMenuExportAll() {
        Path f = FileChooserUtil.saveFile(stage, SQLITE_EXT);
        //noinspection ConstantConditions
        if (f != null) {
            throw new RuntimeException("NotImplemented");
            //db.exportAll(f);
        }
    }
}
