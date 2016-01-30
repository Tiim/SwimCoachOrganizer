package ch.tiim.sco.gui.main;

import ch.tiim.sco.gui.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.InputStream;


public abstract class MainView extends View {

    private Menu menu = new Menu("Edit");

    protected static MenuItem createItem(String name, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(name);
        item.setOnAction(handler);
        return item;
    }

    public void opened() {

    }

    public InputStream getIcon() {
        return getClass().getResourceAsStream(getClass().getSimpleName() + ".png");
    }

    public Menu getMenu() {
        return menu;
    }

    public String getName() {
        return getClass().getSimpleName();
    }
}
