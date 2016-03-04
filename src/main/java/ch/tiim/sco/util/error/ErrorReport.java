package ch.tiim.sco.util.error;

import ch.tiim.sco.util.MultipartPostUtil;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.LogManager;

/**
 * Created by timba on 04.03.2016.
 */
public class ErrorReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorReport.class);
    private static final String URL = "";

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

    public String generateReport() {
        try {
            MultipartPostUtil mp = new MultipartPostUtil(URL, Charsets.UTF_8);
        } catch (IOException e) {

        }
        return finalReport;
    }

    public void send() {

    }
}
