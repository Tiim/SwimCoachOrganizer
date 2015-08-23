package ch.tiim.updater;

import ch.tiim.updater.metadata.Metadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Error codes:
 * 1 - Wrong argument count
 * 2 - Download error
 * 3 - Execution error
 * <p>
 * Arguments:
 * Download url
 * Execution command
 *
 * @author Tim
 * @since 07 - 2014
 */
public final class UpdaterMain {

    private static final String REMOTE_BASE_URL = "https://dl.dropboxusercontent.com/u/49598155/sm/";
    private static final String REMOTE_APP_URL = REMOTE_BASE_URL + "dist.zip";
    private static final String LOCAL_UPDATER_VERSION = "updaterVersion.txt";
    private static final ProgressDialog dialog = new ProgressDialog();

    private static Metadata metadata;

    public static void main(final String[] args) throws InterruptedException {
        try {

            boolean parentIsDead = false;
            do {
                //TODO REMOVE THIS AS SOON AS THE PROGRAM GETS STABLE
                try {
                    Files.deleteIfExists(Paths.get("file.db"));
                    parentIsDead = true;
                } catch (IOException e) {
                    dialog.message("Parent has not yet terminated! waiting 5sec...");
                    Thread.sleep(5000);
                }
            } while (!parentIsDead);

            downloadAndExtract();
            dialog.setProgress(99);
            dialog.message("Version: " + metadata.getBuild().getVersion());
            dialog.message("Build: " + metadata.getBuild().getDate());
            launchProgram();
            dialog.setProgress(100);
            if (UpdaterMain.class.getPackage().getImplementationVersion() != null) {
                Files.write(Paths.get(LOCAL_UPDATER_VERSION),
                        UpdaterMain.class.getPackage().getImplementationVersion().getBytes(),
                        StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                Files.deleteIfExists(Paths.get(LOCAL_UPDATER_VERSION));
            }
        } catch (IOException e) {
            dialog.message(e.getLocalizedMessage());
        }
        Thread.sleep(3000);
        System.exit(0);
    }

    private static void launchProgram() throws IOException {
        dialog.message("Launching the new version");
        dialog.message("Execute " + metadata.getLaunch().getArg());
        Runtime.getRuntime().exec(metadata.getLaunch().getArg());
    }

    public static void downloadAndExtract() throws IOException {
        URL url = new URL(REMOTE_APP_URL);
        URLConnection conn = url.openConnection();
        long size = conn.getContentLengthLong();
        File output = new File(".");
        dialog.message("Downloading " + url);
        dialog.message(" -> " + size);
        ByteCountingInputStream.Updater u = i -> dialog.setProgress((int) (((float) i * 100) / ((float) size)));
        try (ByteCountingInputStream bc = new ByteCountingInputStream(conn.getInputStream(), u)) {
            ZipInputStream zis = new ZipInputStream(bc);
            ZipEntry e = zis.getNextEntry();
            while (e != null) {
                if (!e.isDirectory() && !e.getName().equals("meta.xml")) {
                    final String zipFileName = e.getName();
                    final File zipNewFile = new File(output, zipFileName);
                    dialog.message("Extracting " + zipFileName + " to " + zipNewFile);
                    final File parent = new File(zipNewFile.getParent());
                    if (!parent.equals(output) && parent.mkdirs()) {
                        dialog.message("Could not make folder " + zipNewFile.getParent());
                    }
                    try (FileOutputStream fos = new FileOutputStream(zipNewFile)) {
                        int len;
                        final byte[] buffer = new byte[1024];
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                } else if (e.getName().equals("meta.xml")) {
                    System.out.println("META");
                    parseMetadata(new UnclosableInputStream(zis));
                }
                e = zis.getNextEntry();
            }
        }
    }

    private static void parseMetadata(InputStream is) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(Metadata.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            metadata = (Metadata) unmarshaller.unmarshal(is);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }
}
