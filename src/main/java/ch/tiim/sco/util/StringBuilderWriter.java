package ch.tiim.sco.util;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;


public class StringBuilderWriter extends Writer {

    private final StringBuilder sb;

    public StringBuilderWriter(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void write(@Nullable char[] cbuf, int off, int len) throws IOException {
        sb.append(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
