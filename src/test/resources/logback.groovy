appender('CONSOLE', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{"ISO8601", UTC}  %p %t %c{0}.%M - %m%n'
    }
}

logger('ch.tiim.sco.database', WARN)

root(TRACE, ['CONSOLE'])