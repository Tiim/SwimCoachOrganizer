package ch.tiim.sco.util.error;

import ch.tiim.sco.util.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ErrorReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorReport.class);
    private static final String SERVER_URL = "";

    private Throwable throwable;
    private ArrayList<Path> files = new ArrayList<>(2);
    private String userMessage;
    private String finalReport;

    public ErrorReport() {

    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void setThrowable(Throwable t) {
        this.throwable = throwable;
    }

    public void addFile(Path p) {
        files.add(p);
    }

    public String generateReport() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("=== USER ===\n").append(userMessage).append("\n");
        throwable.printStackTrace(new PrintWriter(new StringBuilderWriter(sb)));
        sb.append("\n");
        for (Path p : files) {
            sb.append("=== ").append(p.getFileName().toString()).append(" ===");
            List<String> l = Files.readAllLines(p);
            for (String s : l) {
                sb.append(l).append("\n");
            }
        }
        finalReport = sb.toString();
        return finalReport;
    }

    public void send() throws IOException {
        URL url = new URL(SERVER_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "plain/text");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(finalReport.length()));
        conn.setUseCaches(false);
        try (PrintWriter wr = new PrintWriter(conn.getOutputStream())) {
            wr.write(finalReport);
        }
        InputStream is = conn.getInputStream();
    }
}
