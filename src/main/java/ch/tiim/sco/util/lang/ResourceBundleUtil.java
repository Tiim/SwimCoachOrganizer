package ch.tiim.sco.util.lang;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleUtil {

    private static final String RESOURCE_PATH = "ch/tiim/sco/lang/lang"; //NON-NLS

    public static ResourceBundle getResourceBundle(Locale l) {
        return ResourceBundle.getBundle(RESOURCE_PATH, l);
    }

}
