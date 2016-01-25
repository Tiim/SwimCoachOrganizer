package ch.tiim.sco.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JSHelper {
    public static List<Method> getFuctions(Object o) {
        Class<?> clazz = o.getClass();
        List<Method> m = Arrays.asList(clazz.getMethods());
        return m;
    }
}
