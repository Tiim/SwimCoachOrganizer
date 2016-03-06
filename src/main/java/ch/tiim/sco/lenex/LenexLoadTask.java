package ch.tiim.sco.lenex;

import ch.tiim.sco.lenex.model.Lenex;
import ch.tiim.sco.util.ByteCountingInputStream;
import ch.tiim.sco.util.lang.ResourceBundleEx;
import javafx.concurrent.Task;

import java.nio.file.Path;

public class LenexLoadTask extends Task<Lenex> {

    private final Path path;
    private final ResourceBundleEx lang;

    public LenexLoadTask(Path path, ResourceBundleEx lang) {
        this.path = path;
        this.lang = lang;
    }

    @Override
    protected Lenex call() throws Exception {
        LenexParser parser = new LenexParser();
        updateMessage(lang.str("task.lenex.parse"));
        Lenex lenex = parser.read(path, new Updater());
        updateMessage(lang.str("task.done"));
        return lenex;
    }

    private class Updater implements ByteCountingInputStream.Updater {
        private long max = 1;

        @Override
        public void update(long i) {
            updateProgress(i, max);
        }

        @Override
        public void setMax(long i) {
            max = i;
        }
    }
}
