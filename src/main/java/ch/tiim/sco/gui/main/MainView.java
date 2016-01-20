package ch.tiim.sco.gui.main;

import ch.tiim.sco.gui.View;

import java.io.InputStream;


public abstract class MainView extends View {

    public void opened() {

    }

    public InputStream getIcon() {
        return getClass().getResourceAsStream(getClass().getSimpleName() + ".png");
    }

    public String getName() {
        return getClass().getSimpleName();
    }

}
