package ch.tiim.sco.database.export;


public class ExportException extends Exception {
    public ExportException() {
        super();
    }

    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExportException(Throwable cause) {
        super(cause);
    }

    protected ExportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
