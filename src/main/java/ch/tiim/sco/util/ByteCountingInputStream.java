package ch.tiim.sco.util;

import javax.annotation.Nonnull;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */

/**
 * @author clint
 */
public class ByteCountingInputStream extends FilterInputStream {

    private final Updater updater;
    private long totalRead = 0;

    public ByteCountingInputStream(InputStream in, Updater u) {
        super(in);
        updater = u;
    }

    @Override
    public int read() throws IOException {
        int ret = super.read();
        totalRead++;
        updater.update(totalRead);
        return ret;
    }

    @Override
    public int read(@Nonnull byte[] b) throws IOException {
        int ret = super.read(b);
        totalRead += ret;
        updater.update(totalRead);
        return ret;
    }

    @Override
    public int read(@Nonnull byte[] b, int off, int len) throws IOException {
        int ret = super.read(b, off, len);
        totalRead += ret;
        updater.update(totalRead);
        return ret;
    }

    @Override
    public long skip(long n) throws IOException {
        totalRead += n;
        updater.update(totalRead);
        return super.skip(n);
    }

    /**
     * @return the totalRead
     */
    public long getTotalRead() {
        return this.totalRead;
    }

    public interface Updater {
        void update(long i);

        void setMax(long i);
    }

}