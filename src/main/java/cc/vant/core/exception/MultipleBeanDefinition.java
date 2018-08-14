package cc.vant.core.exception;

/**
 * @author Vant
 * @version 2018/8/15 上午 12:08
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
