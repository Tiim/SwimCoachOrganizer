package ch.tiim.sco.util.error;

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
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n=== SPEC ===\n");
        sb.append("Java Version ").append(System.getProperty("java.version")).append("\n");
        sb.append("User Name ").append(System.getProperty("user.name")).append("\n");
        sb.append("OS ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version"))
                .append("\n");
        sb.append("JavaFx ").append(System.getProperty("javafx.version")).append("\n");
        sb.append("JVM ").append(System.getProperty("java.vm.vendor")).append(" ")
                .append(System.getProperty("java.vm.version")).append("\n");
        sb.append("Arch ").append(System.getProperty("os.arch")).append("\n");
        return sb.toString();
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
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
