package ch.tiim.sco.util.async;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;

public class DaemonFactory implements ThreadFactory {
    @Override
    public Thread newThread(@Nonnull Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("EXECUTOR THREAD"); //NON-NLS
        return t;
    }
}
