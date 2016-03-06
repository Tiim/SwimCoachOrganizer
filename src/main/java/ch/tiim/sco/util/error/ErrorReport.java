package ch.tiim.sco.util.error;

import ch.tiim.sco.update.VersionChecker;
import ch.tiim.sco.util.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class ErrorReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorReport.class);
    private static final String SERVER_URL = "https://n.ethz.ch/student/batim/php/sco_report.php";

    private Throwable throwable;
    private ArrayList<Path> files = new ArrayList<>(2);
    private String report;

    public ErrorReport() {

    }

    public void setThrowable(Throwable t) {
        this.throwable = t;
    }

    public void addFile(Path p) {
        files.add(p);
    }

    private String osInfo() {
        return "\n\n=== SPEC ===\n" +
                "Java Version " + System.getProperty("java.version") + "\n" +
                "User Name " + System.getProperty("user.name") + "\n" +
                "OS " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
                "\n" +
                "JavaFx " + System.getProperty("javafx.version") + "\n" +
                "JVM " + System.getProperty("java.vm.vendor") + " " +
                System.getProperty("java.vm.version") + "\n" +
                "Arch " + System.getProperty("os.arch") + "\n";
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Version: ").append(VersionChecker.getCurrentVersion()).append("\n\n");
        throwable.printStackTrace(new PrintWriter(new StringBuilderWriter(sb)));
        sb.append("\n");
        sb.append(osInfo());
        for (Path p : files) {
            sb.append("\n\n=== ").append(p.getFileName().toString()).append(" ===\n");
            try {
                List<String> l = Files.readAllLines(p);
                for (String s : l) {
                    sb.append(s).append("\n");
                }
            } catch (IOException e) {
                sb.append("Can't read line: ").append(e);
            }
        }
        report = sb.toString();
        return report;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String r) {
        report = r;
    }

    public void send() throws IOException {
        URL url = new URL(SERVER_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "plain/text");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(report.length()));
        conn.setUseCaches(false);
        try (PrintWriter wr = new PrintWriter(conn.getOutputStream())) {
            wr.write(report);
        }
        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }
    }
}
