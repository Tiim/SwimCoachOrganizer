package ch.tiim.sco.gui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Splash extends BorderPane {

    private static final String IMG_PATH = "splash.png";

    private ImageView img;
    private ProgressBar bar;

    public Splash() {
        img = new ImageView(IMG_PATH);
        img.setPreserveRatio(true);
        img.setFitHeight(75);
        bar = new ProgressBar();
        bar.setMaxWidth(Double.MAX_VALUE);
        setCenter(img);
        setBottom(bar);
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
