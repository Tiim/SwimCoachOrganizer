package ch.tiim.sco.util.async;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutorEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorEventListener.class);
    private final ScheduledThreadPoolExecutor e;

    public ExecutorEventListener(ScheduledThreadPoolExecutor e) {
        this.e = e;
    }

    @Subscribe
    public void onRunnable(Runnable r) {
        LOGGER.trace("Running " + r); //NON-NLS
        e.execute(r);
    }
}
