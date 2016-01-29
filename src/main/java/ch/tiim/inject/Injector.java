package ch.tiim.inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used to register objects to be injected into classes
 */
public class Injector {
    private static final Logger LOGGER = LogManager.getLogger(Injector.class.getName());
    private static final Injector INSTANCE = new Injector();
    private static final String INJECTED_CALLBACK = "injected";
    private final HashMap<String, Object> toInject = new HashMap<>();

    /**
     * Private constructor
     */
    private Injector() {
    }

    /**
     * Register an object to be injected into other objects
     *
     * @param o    the object to inject
     * @param name the name of the object
     */
    public void addInjectable(Object o, String name) {
        toInject.put(name, o);
    }

    /**
     * Inject the registered items into object
     * @param o the object into which items will be injected
     * @param customInjections a custom map of items to inject
     */
    public void inject(Object o, Map<String, Object> customInjections) {
        try {
            injectFields(o, customInjections);
            callMethod(o);
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Error while injecting " + o.getClass());
            throw new IllegalStateException(e);
        }
    }

    private void injectFields(Object o, Map<String, Object> customInjections) throws IllegalAccessException, InstantiationException {

        List<Field> fields = getSuperClasses(o.getClass())
                .stream()
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

            for (Field f : fields) {
                Inject a = f.getAnnotation(Inject.class);
                if (a == null) {
                    continue;
                }
                f.setAccessible(true);
                if (a.newInstance()) {
                    f.set(o, f.getType().newInstance());
                } else {
                    Object obj;
                    if (customInjections != null && customInjections.containsKey(a.name())) {
                        obj = customInjections.get(a.name());
                    } else if (toInject.containsKey(a.name())) {
                        obj = toInject.get(a.name());
                    } else {
                        LOGGER.warn("Injectable object with key " + a.name() + " not found.");
                        continue;
                    }
                    if (f.getType().isInstance(obj)) {
                        f.set(o, obj);
                    }
                }
        }
    }

    private void callMethod(Object o) {
        Class<?> clazz = o.getClass();
        try {
            Method method = clazz.getDeclaredMethod(INJECTED_CALLBACK);
            method.setAccessible(true);
            Inject i = method.getAnnotation(Inject.class);
            if (i != null) {
                try {
                    method.invoke(o);
                } catch (Exception ex) {
                    LOGGER.warn("Exception in injected callback: ", ex);
                }
            }
        } catch (NoSuchMethodException ignored) {
        }
    }

    private List<Class<?>> getSuperClasses(Class<?> clazz) {
        List<Class<?>> res = new ArrayList<>();
        do {
            res.add(clazz);
            clazz = clazz.getSuperclass();
        } while (clazz != null && !clazz.getCanonicalName().equals("java.lang.Object"));
        return res;
    }

    public static Injector getInstance() {
        return INSTANCE;
    }
}
