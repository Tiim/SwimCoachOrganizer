package ch.tiim.updater;

import java.io.IOException;
import java.io.InputStream;

public class UnclosableInputStream extends InputStream {


    private final InputStream is;

    public UnclosableInputStream(InputStream is) {

        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public void close() throws IOException {
        //DO NOTHING
    }
}
