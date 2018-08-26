package cc.vant.tinyspring.core.exception;

/**
 * @author Vant
 * @since 2018/8/4 14:20
 */
public class NoSuchBeanDefinitionException extends RuntimeException {
    public NoSuchBeanDefinitionException() {
    }

    public NoSuchBeanDefinitionException(String name) {
        super("No bean Named: " + name);
    }

    public NoSuchBeanDefinitionException(String name, Throwable cause) {

        super("No bean Named: " + name, cause);
    }

    public NoSuchBeanDefinitionException(Throwable cause) {
        super(cause);
    }

    public NoSuchBeanDefinitionException(String name, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("No bean Named: " + name, cause, enableSuppression, writableStackTrace);
    }
}
