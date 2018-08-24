package cc.vant.tinyspring.core.exception;

/**
 * @author Vant
 * @version 2018/8/9 下午 9:01
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
