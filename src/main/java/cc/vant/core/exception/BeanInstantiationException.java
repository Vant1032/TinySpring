package cc.vant.core.exception;

/**
 * @author Vant
 * @version 2018/8/9 下午 2:24
 */
public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException() {
    }

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanInstantiationException(Throwable cause) {
        super(cause);
    }

    public BeanInstantiationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
