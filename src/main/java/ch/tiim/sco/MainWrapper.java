package ch.tiim.sco;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Wraps the default main class, so that people with an outdated java version get an error message.
 *
 * @author Tim
 * @since 07 - 2014
 */

@SuppressWarnings("HardCodedStringLiteral")
public final class MainWrapper {
    private static final float MIN_JAVA_VERSION_REQUIRED = 1.8f;

    /**
     * Main method
     *
     * @param args Command line arguments
     */
    @SuppressWarnings({"TryWithIdenticalCatches", "unchecked"})
    public static void main(final String[] args) {
        if (!checkVersion()) {
            try {
                // Maybe the class can be used, idk :D
                final Class mainClass = Class.forName("Main");
                final Method mainMethod = mainClass.getMethod("main", String[].class);
                mainMethod.invoke(null, (Object) args);
            } catch (final Exception e) {
                System.err.println("Can't start application:");
                e.printStackTrace(System.err);
                JOptionPane.showMessageDialog(null, "Can't start the application!", "Old Java Version",
                        JOptionPane.ERROR_MESSAGE);
            } catch (final UnsupportedClassVersionError e) {
                System.err.println("Can't start application:");
                e.printStackTrace(System.err);
                JOptionPane.showMessageDialog(null, "Can't start the application!", "Old Java Version",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Found java version: " + System.getProperty("java.specification.version")
                    + "(" + System.getProperty("java.version") + ")");
            Main.main(args);
        }
    }

    /**
     * Checks if the java version is equal or greater than {@link #MIN_JAVA_VERSION_REQUIRED}
     *
     * @return true if the java version is supported
     */
    private static boolean checkVersion() {
        final String version = System.getProperty("java.specification.version");
        final float v = Float.parseFloat(version);
        if (v < MIN_JAVA_VERSION_REQUIRED) {
            final String w = "Found java version: " + version + "(" + System.getProperty("java.version") + ")\n"
                    + "Minimal version required: " + MIN_JAVA_VERSION_REQUIRED + "\n"
                    + "This program may run unstable";

            System.err.println(w);
            JOptionPane.showMessageDialog(null, w, "Old Java Version", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
