package ch.tiim.sco.lenex;

import ch.tiim.log.Log;
import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.util.ByteCountingInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LenexParser {
    private static final Log LOGGER = new Log(LenexParser.class);
    private static final PathMatcher LENEX_XML = FileSystems.getDefault().getPathMatcher("regex:.*\\.lef");
    private static final PathMatcher LENEX_ZIP = FileSystems.getDefault().getPathMatcher("regex:.*\\.lxf");

    public Lenex read(Path p, ByteCountingInputStream.Updater updater) throws IOException {
        InputStream is = null;
        try {
            if (LENEX_ZIP.matches(p)) {
                LOGGER.debug("Compressed LENEX file");
                ZipFile zf = new ZipFile(p.toFile());
                ZipEntry e = zf.entries().nextElement();
                is = zf.getInputStream(e);
                updater.setMax(e.getSize());
            } else if (LENEX_XML.matches(p)) {
                LOGGER.debug("XML LENEX file");
                is = new FileInputStream(p.toFile());
                updater.setMax(p.toFile().length());
            } else {
                LOGGER.warning(p.toString() + " is not a .lef or .lxf file");
                if (!Files.exists(p)) {
                    LOGGER.warning("It doesn't even exit");
                }
            }

            ByteCountingInputStream bc = new ByteCountingInputStream(is, updater);

            JAXBContext context = JAXBContext.newInstance(Lenex.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Lenex) unmarshaller.unmarshal(bc);
        } catch (JAXBException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            //noinspection EmptyTryBlock
            try (InputStream iss = is) {
                // input stream gets closed.
            }
        }
        return null;
    }
}
