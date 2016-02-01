package ch.tiim.sco.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Logging {

    public static void reloadLoggerConfig() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        ContextInitializer ci = new ContextInitializer(loggerContext);
        URL url = ci.findURLOfDefaultConfigurationFile(true);
        GafferConfigurator configurator = new GafferConfigurator(loggerContext);
        loggerContext.reset();
        configurator.run(url);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

}
