package ch.tiim.sco.gui;

import ch.tiim.inject.Inject;
import ch.tiim.inject.Injector;
import com.google.common.eventbus.EventBus;
import javafx.scene.Parent;

public abstract class View {
    @Inject(name = "event-bus")
    protected EventBus eventBus;

    public View() {
        Injector.getInstance().inject(this, null);
        eventBus.register(this);
    }

    public abstract Parent getRoot();
}