package ch.tiim.sco.print;

import ch.tiim.sco.database.model.IndexedSet;
import ch.tiim.sco.database.model.Set;
import ch.tiim.sco.database.model.Training;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.List;

public class PrinterNode extends VBox {

    private final PrintUtils utils;

    public PrinterNode(Training t, List<IndexedSet> s) {
        utils = new PrintUtils(Printer.getDefaultPrinter());
        setPrefSize(utils.getWidth(), utils.getHeight());
        setMinSize(utils.getWidth(), utils.getHeight());
        setMaxSize(utils.getWidth(), utils.getHeight());
        setId("content");
        addTitle(t);
        addLine();
        addSets(s);
        getChildren().add(new Line(0, 0, 75, 0));
        addSum(s);
        addLine();
    }

    private void addTitle(Training t) {
        Label title = new Label(t.getName());
        title.setId("header");
        getChildren().add(title);

    }

    private void addLine() {
        getChildren().add(new Line(0, 0, utils.getWidth(), 0));
    }

    private void addSets(List<IndexedSet> sets) {
        GridPane pane = new GridPane();

        pane.setHgap(10);

        for (IndexedSet is : sets) {
            Set s = is.getSet();
            Label lblContent = new Label(s.getContent());
            lblContent.setWrapText(true);
            pane.addRow(is.getIndex(),
                    new Label(s.getTotalDistance() + "m"),
                    new Label(s.getDistance()),
                    lblContent,
                    new Label(s.getIntervalString()),
                    new Label(s.getFocus() == null ? "-" : s.getFocus().getAbbr()),
                    new Label(String.valueOf(s.getIntensity())),
                    new Label(s.getStroke() == null ? "-" : s.getStroke().getAbbr())
            );
            RowConstraints rc = new RowConstraints();
            rc.setValignment(VPos.TOP);
            pane.getRowConstraints().add(rc);
        }

        ColumnConstraints cTotalDist = new ColumnConstraints(50, 50, 50, Priority.NEVER, HPos.RIGHT, false);
        ColumnConstraints cDist = new ColumnConstraints(50, 50, 50, Priority.NEVER, HPos.RIGHT, false);
        ColumnConstraints cContent = new ColumnConstraints(100, 200, 2000, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints cInterval = new ColumnConstraints(35, 35, 35, Priority.NEVER, HPos.LEFT, false);
        ColumnConstraints cFocus = new ColumnConstraints(25, 25, 25, Priority.NEVER, HPos.LEFT, false);
        ColumnConstraints cIntensity = new ColumnConstraints(25, 25, 25, Priority.NEVER, HPos.LEFT, false);
        ColumnConstraints cStroke = new ColumnConstraints(25, 25, 25, Priority.NEVER, HPos.LEFT, false);


        pane.getColumnConstraints().add(cTotalDist);
        pane.getColumnConstraints().add(cDist);
        pane.getColumnConstraints().addAll(cContent);
        pane.getColumnConstraints().addAll(cInterval);
        pane.getColumnConstraints().addAll(cFocus);
        pane.getColumnConstraints().addAll(cIntensity);
        pane.getColumnConstraints().addAll(cStroke);

        getChildren().add(pane);
    }

    private void addSum(List<IndexedSet> s) {
        int i = 0;
        for (IndexedSet is : s) {
            i += is.getSet().getTotalDistance();
        }
        float j = (float) i / 1000f;
        Label e = new Label(j + " km");
        e.setId("sum");
        getChildren().add(e);
    }

    public void show() {
        AnchorPane pane = new AnchorPane();
        pane.setId("background");
        addCss(pane);
        AnchorPane.setTopAnchor(this, utils.getMargTop());
        AnchorPane.setLeftAnchor(this, utils.getMargLeft());
        AnchorPane.setBottomAnchor(this, utils.getMargBottom());
        AnchorPane.setRightAnchor(this, utils.getMargRight());

        pane.getChildren().add(this);

        Scene s = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(s);
        stage.setTitle("Print Preview");
        stage.show();
        stage.setResizable(false);
    }

    private void addCss(Parent p) {
        p.getStylesheets().add(PrinterNode.class.getResource("print.css").toExternalForm());
    }

    public void print() {
        addCss(this);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            boolean success = job.printPage(this);
            if (success) {
                job.endJob();
            }
        }
    }
}
