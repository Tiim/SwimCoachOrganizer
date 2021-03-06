package ch.tiim.sco.util.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class ResourceBundleEx extends ResourceBundle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleEx.class);

    private final ResourceBundle rb;

    public ResourceBundleEx(ResourceBundle rb) {

        this.rb = rb;
    }

    public String format(String key, String... keys) {
        String[] vals = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            vals[i] = str(keys[i]);
        }
        return String.format(str(key), (Object[]) vals);
    }

    public String str(String key) {
        try {
            return getString(key);
        } catch (Exception e) {
            LOGGER.warn("Localisation for [" + key + "] does not exist."); //NON-NLS
            return "[" + key + "]";
        }
    }

    @Override
    protected Object handleGetObject(@Nonnull String key) {
        return rb.getObject(key);
    }

    @Nonnull
    @Override
    public Enumeration<String> getKeys() {
        return rb.getKeys();
    }

    @Override
    public String getBaseBundleName() {
        return rb.getBaseBundleName();
    }

    @Override
    public Locale getLocale() {
        return rb.getLocale();
    }

    @Override
    protected void setParent(ResourceBundle parent) {
    }

    @Override
    public boolean containsKey(@Nonnull String key) {
        return rb.containsKey(key);
    }

    @Nonnull
    @Override
    public Set<String> keySet() {
        return rb.keySet();
    }

    @Nonnull
    @Override
    protected Set<String> handleKeySet() {
        return super.handleKeySet();
    }
}
