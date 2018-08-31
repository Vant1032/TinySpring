package cc.vant.tinyspring.webmvc.annotation;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * @author Vant
 * @since 2018/8/31 23:16
 */

public class SpringServletContainerInitializer implements ServletContainerInitializer {

    /**
     *
     * @param c
     * @param ctx
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

    }
}
