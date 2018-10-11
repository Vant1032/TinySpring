package cc.vant.tinyspring.webmvc;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * @author Vant
 * @since 2018/8/31 23:16
 */
@HandlesTypes(value = WebApplicationInitializer.class)
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
