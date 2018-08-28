package cc.vant.tinyspring.core.exception;

/**
 * @author Vant
 * @since 2018/8/15 12:08
 */
public class MultipleBeanDefinition extends RuntimeException {
    public MultipleBeanDefinition() {
    }

    public MultipleBeanDefinition(String message) {
        super(message);
    }

    public MultipleBeanDefinition(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleBeanDefinition(Throwable cause) {
        super(cause);
    }

    public MultipleBeanDefinition(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
