package ch.tiim.sco.util.init;

import ch.tiim.sco.util.lang.ResourceBundleEx;
import ch.tiim.sco.util.lang.ResourceBundleUtil;
import javafx.concurrent.Task;

import java.util.Locale;

public class LoadLocale extends Task<ResourceBundleEx> {
    private Locale locale;

    @Override
    protected ResourceBundleEx call() throws Exception {
        return new ResourceBundleEx(ResourceBundleUtil.getResourceBundle(locale));
    }

    public void setLocale(Locale locale) {
        if (getState() != State.READY) {
            throw new IllegalStateException("Locale must be set before the task is started");
        }
        this.locale = locale;
    }
}
