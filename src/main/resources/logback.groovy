import ch.qos.logback.ext.loggly.LogglyAppender
import ch.tiim.sco.config.Config
import ch.tiim.sco.config.Settings
import ch.tiim.sco.update.VersionChecker
import com.github.zafarkhaja.semver.Version

//Get tag based on if this is a development version or
//a released version
def Version vers = VersionChecker.getCurrentVersion();
def String v = ''
if (vers.equals(Version.forIntegers(0))) {
    v = 'dev'
} else {
    v = "prod,${vers.toString()}"
}

def appenders = []


appender('CONSOLE', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{"ISO8601", UTC}  %p %t %c{0}.%M - %m%n'
    }
}
appenders << 'CONSOLE'


if (Settings.INSTANCE.getBoolean('loggly.enabled', false) && Config.INSTANCE.getBoolean("loggly.enabled")) {
    appender('LOGGLY', LogglyAppender) {
        endpointUrl = "${Config.INSTANCE.getString("loggly.url")}/tag/logback,${v}"
        pattern = '%d{"ISO8601", UTC}  %p %t %c{0}.%M - %m%n'
    }
    appenders << 'LOGGLY'
    println('Loggly is activated')
}



logger('ch.tiim.sco.database', WARN)

root(TRACE, appenders)