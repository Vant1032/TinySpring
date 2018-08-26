package cc.vant.tinyspring.core.exception;

/**
 * @author Vant
 * @since 2018/8/9 21:01
 */
public class SpringInitException extends RuntimeException {
    public SpringInitException() {
    }

    public SpringInitException(String message) {
        super(message);
    }

    public SpringInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringInitException(Throwable cause) {
        super(cause);
    }

    public SpringInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
