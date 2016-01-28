package ch.tiim.sco.gui;

import ch.tiim.inject.Inject;
import ch.tiim.inject.Injector;
import com.google.common.eventbus.EventBus;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class View {
    protected static final Logger LOGGER = LogManager.getLogger(View.class.getName());
    @Inject(name = "event-bus")
    protected EventBus eventBus;

    public View() {
        Injector.getInstance().inject(this, null);
        eventBus.register(this);
    }

    public abstract Parent getRoot();
}