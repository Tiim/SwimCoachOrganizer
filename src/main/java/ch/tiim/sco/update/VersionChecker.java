package ch.tiim.sco.update;


import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Tim
 * @since 07 - 2014
 */
public final class VersionChecker {
    private static Version currentVersion = null;
    private static Version remoteVersion = null;

    public static void overrideCurrentVersion(Version v) {
        currentVersion = v;
    }

    public static void overrideRemoteVersion(Version v) {
        remoteVersion = v;
    }

    public static boolean isNewUpdaterVersionAvailable() {
        Logger LOGGER = LoggerFactory.getLogger(VersionChecker.class);
        try {
            final Version remote = Version.valueOf(Constants.downloadString(Constants.REMOTE_UPDATER_VERSION_URL));
            if (!Files.exists(Paths.get(Constants.LOCAL_UPDATER_VERSION_URL))) {
                return true;
            }
            final Version local = Version.valueOf(Constants.readString(Constants.LOCAL_UPDATER_VERSION_URL));
            return local.compareTo(remote) < 0;
        } catch (final IOException | IllegalArgumentException e) {
            LOGGER.warn("", e);
        }
        return true;
    }

    public static boolean isNewVersionAvailable() {
        final Version current = getCurrentVersion();
        final Version remote = getRemoteVersion();

        return remote.greaterThan(current) && !current.equals(Version.forIntegers(0));

    }

    public static Version getCurrentVersion() {
        if (currentVersion != null) {
            return currentVersion;
        }
        final String version = VersionChecker.class.getPackage().getImplementationVersion();
        if (version != null) {
            currentVersion = Version.valueOf(version);
        } else {
            currentVersion = Version.forIntegers(0);
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
        Logger LOGGER = LoggerFactory.getLogger(VersionChecker.class);
        Version v;
        try {
            v = Version.valueOf(Constants.downloadString(Constants.REMOTE_PROGRAM_VERSION_URL));
        } catch (Exception e) {
            LOGGER.info("Can't download remote version string: " + e.getMessage());
            v = Version.forIntegers(0);
        }
        remoteVersion = v;
    }
}
