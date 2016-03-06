package ch.tiim.sco.print;

import ch.tiim.sco.database.DatabaseController;
import ch.tiim.sco.database.export.ExportController;
import ch.tiim.sco.database.model.Training;
import ch.tiim.sco.util.lang.ResourceBundleEx;
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
    private final ResourceBundleEx lang;

    public PrintTask(Training training, Path output, DatabaseController db, ResourceBundleEx lang) {
        this.training = training;
        this.output = output;
        this.db = db;
        this.lang = lang;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage(lang.str("task.init"));
        updateProgress(0, 3);
        ExportController ex = new ExportController(db);
        ex.addData(training);
        updateMessage(lang.str("task.pdf.export"));
        updateProgress(1, 3);
        Document export = ex.export();
        updateMessage(lang.str("task.pdf.convert"));
        updateProgress(2, 3);
        transformXML(export);
        updateMessage(lang.str("task.done"));
        updateProgress(3, 3);
        return null;
    }

    public void transformXML(Document doc) throws Exception {
        try (OutputStream stream = Files.newOutputStream(output)) {
            Fop fop = FopFactory.newInstance(PrintTask.class.getResource("fop.xml").toURI())  //NON-NLS
                    .newFop(MimeConstants.MIME_PDF, stream);
            TransformerFactory tf = TransformerFactory.newInstance();
            Source xslt = new StreamSource(PrintTask.class.getResourceAsStream("training.xslt")); //NON-NLS
            Transformer transformer = tf.newTransformer(xslt);
            Source xml = new DOMSource(doc);
            transformer.transform(xml, new SAXResult(fop.getDefaultHandler()));
        }
    }
}
