package ch.tiim.sco.update;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Tim
 * @since 07 - 2014
 */
public final class VersionChecker {
    private static final Logger LOGGER = LogManager.getLogger(VersionChecker.class.getName());
    private static Version currentVersion = null;
    private static Version remoteVersion = null;

    public static void overrideCurrentVersion(Version v) {
        currentVersion = v;
    }

    public static void overrideRemoteVersion(Version v) {
        remoteVersion = v;
    }

    public static boolean isNewUpdaterVersionAvailable() {
        try {
            final Version remote = new Version(Constants.downloadString(Constants.REMOTE_UPDATER_VERSION_URL));
            if (!Files.exists(Paths.get(Constants.LOCAL_UPDATER_VERSION_URL))) {
                return true;
            }
            final Version local = new Version(Constants.readString(Constants.LOCAL_UPDATER_VERSION_URL));
            return local.compareTo(remote) < 0;
        } catch (final IOException | IllegalArgumentException e) {
            LOGGER.warn("", e);
        }
        return true;
    }

    public static boolean isNewVersionAvailable() {
        final Version current = getCurrentVersion();
        final Version remote = getRemoteVersion();

        return current.isDeployed() && remote.isDeployed() && remote.newerThan(current);

    }

    public static Version getCurrentVersion() {
        if (currentVersion != null) {
            return currentVersion;
        }
        final String version = VersionChecker.class.getPackage().getImplementationVersion();
        if (version != null) {
            currentVersion = new Version(version);
        } else {
            currentVersion = new Version();
        }
        return currentVersion;
    }

    public static Version getRemoteVersion() {
        if (remoteVersion != null) {
            return remoteVersion;
        }
        reloadRemoteVersion();
        return remoteVersion;
    }

    public static void reloadRemoteVersion() {
        Version v;
        try {
            v = new Version(Constants.downloadString(Constants.REMOTE_PROGRAM_VERSION_URL));
        } catch (final IOException e) {
            LOGGER.warn("", e);
            v = new Version();
        }
        remoteVersion = v;
    }
}
