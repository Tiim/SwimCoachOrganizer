package ch.tiim.sco.print;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.export.ExportController;
import ch.tiim.sco.database.model.Training;
import javafx.concurrent.Task;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrintTask extends Task<Void> {

    private final Training training;
    private final Path output;
    private final DatabaseController db;

    public PrintTask(Training training, Path output, DatabaseController db) {
        this.training = training;
        this.output = output;
        this.db = db;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Initializing");
        updateProgress(0, 3);
        ExportController ex = new ExportController(db);
        ex.addData(training);
        updateMessage("Exporting data to xml");
        updateProgress(1, 3);
        Document export = ex.export();
        updateMessage("Converting to pdf");
        updateProgress(2, 3);
        transformXML(export);
        updateMessage("Done");
        updateProgress(3, 3);
        return null;
    }

    public void transformXML(Document doc) throws Exception {
        try (OutputStream stream = Files.newOutputStream(output)) {
            Fop fop = FopFactory.newInstance(PrintTask.class.getResource("fop.xml").toURI())
                    .newFop(MimeConstants.MIME_PDF, stream);
            TransformerFactory tf = TransformerFactory.newInstance();
            Source xslt = new StreamSource(PrintTask.class.getResourceAsStream("training.xslt"));
            Transformer transformer = tf.newTransformer(xslt);
            Source xml = new DOMSource(doc);
            transformer.transform(xml, new SAXResult(fop.getDefaultHandler()));
        }
    }
}
