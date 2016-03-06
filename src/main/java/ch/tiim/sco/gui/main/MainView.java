package ch.tiim.sco.gui.main;

import ch.tiim.inject.Inject;
import ch.tiim.sco.gui.View;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.InputStream;


public abstract class MainView extends View {

    @Inject(name = "lang")
    private ResourceBundleEx lang;

    private Menu menu;

    protected static MenuItem createItem(String name, BooleanProperty isEnabled, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(name);
        item.setOnAction(handler);
        if (isEnabled != null) {
            item.disableProperty().bind(isEnabled.not());
        }
        return item;
    }

    public void opened() {

    }

    public InputStream getIcon() {
        return getClass().getResourceAsStream(getClass().getSimpleName() + ".png"); //NON-NLS
    }

    public Menu getMenu() {
        if (menu == null) {
            menu = new Menu(lang.str("gui." + getClass().getSimpleName().replace("View", "").toLowerCase())); //NON-NLS
        }
        return menu;
    }

    public String getName() {
        return getClass().getSimpleName();
    }
}
