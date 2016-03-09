package ch.tiim.sco.util.init;

import ch.tiim.sco.util.lang.ResourceBundleEx;
import ch.tiim.sco.util.lang.ResourceBundleUtil;
import javafx.concurrent.Task;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoadLocale extends Task<ResourceBundleEx> {
    private Locale locale;

    @Override
    protected ResourceBundleEx call() throws Exception {
        updateProgress(0, 10);
        ResourceBundle rb = ResourceBundleUtil.getResourceBundle(locale);
        updateProgress(10, 10);
        return new ResourceBundleEx(rb);
    }

    public void setLocale(Locale locale) {
        if (getState() != State.READY) {
            throw new IllegalStateException("Locale must be set before the task is started");
        }
        this.locale = locale;
    }
}
