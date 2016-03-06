package ch.tiim.sco.update;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("HardCodedStringLiteral")
public class Constants {

    public static final String LOCAL_UPDATER_URL = "Updater.jar";
    public static final String LOCAL_UPDATER_VERSION_URL = "updaterVersion.txt";
    public static final String LAUNCH_UPDATER = "java -jar " + LOCAL_UPDATER_URL;
    private static final String REMOTE_BASE_URL = "https://dl.dropboxusercontent.com/u/49598155/sm/";
    public static final String REMOTE_UPDATER_URL = REMOTE_BASE_URL + "Updater.jar";
    public static final String REMOTE_UPDATER_VERSION_URL = REMOTE_BASE_URL + "updaterVersion.txt";
    public static final String REMOTE_PROGRAM_VERSION_URL = REMOTE_BASE_URL + "version.txt";

    public static String downloadString(final String url) throws IOException {
        final URL remote = new URL(url);
        final URLConnection conn = remote.openConnection();
        final BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
        final byte[] contents = new byte[20];
        int bytesRead;
        String str = "";
        while ((bytesRead = is.read(contents)) != -1) {
            str += new String(contents, 0, bytesRead);
        }
        return str.trim();
    }

    public static String readString(final String file) throws IOException {
        final BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        final byte[] contents = new byte[20];
        int bytesRead;
        String str = "";
        while ((bytesRead = is.read(contents)) != -1) {
            str += new String(contents, 0, bytesRead);
        }
        return str.trim();
    }

    public static void downloadFile(final String url, final String file) throws IOException {
        final URL remote = new URL(url);
        final URLConnection conn = remote.openConnection();
        final InputStream is = conn.getInputStream();
        final FileOutputStream fos = new FileOutputStream(file);
        final byte[] contents = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(contents)) != -1) {
            fos.write(contents, 0, bytesRead);
        }
    }
}
