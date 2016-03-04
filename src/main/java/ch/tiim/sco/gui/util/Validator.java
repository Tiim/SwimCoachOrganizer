package ch.tiim.sco.gui.util;

/**
 * Created by timba on 03.03.2016.
 */
public class Validator {

    public static String strNotEmpty(String s, String name) throws UIException {
        if (s == null || s.isEmpty()) {
            throw new UIException(name);
        }
        return s;
    }

    public static <T> T nonNull(T obj, String name) throws UIException {
        if (obj == null) {
            throw new UIException(name);
        }
        return obj;
    }

}
