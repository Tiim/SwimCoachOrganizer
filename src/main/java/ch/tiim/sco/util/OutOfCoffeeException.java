package ch.tiim.sco.util;

public class OutOfCoffeeException extends RuntimeException {
    public OutOfCoffeeException() {
        super();
    }

    public OutOfCoffeeException(String message) {
        super(message);
    }

    public OutOfCoffeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfCoffeeException(Throwable cause) {
        super(cause);
    }

    protected OutOfCoffeeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
