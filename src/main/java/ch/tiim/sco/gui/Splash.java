package ch.tiim.sco.gui;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Splash extends BorderPane {

    private ProgressBar bar;

    public Splash(Stage primaryStage) {
        bar = new ProgressBar();
        bar.setMaxWidth(Double.MAX_VALUE);
        setBottom(bar);
        primaryStage.setWidth(300);
        primaryStage.setHeight(150);
    }


    public void setProgress(double p) {
        bar.setProgress(p);
    }

    public DoubleProperty progressProperty() {
        return bar.progressProperty();
    }

    public double getProgress() {
        return bar.getProgress();
    }

}
