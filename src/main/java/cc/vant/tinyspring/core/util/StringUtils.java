package cc.vant.tinyspring.core.util;

import cc.vant.tinyspring.core.BeanContainer;
import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.exception.BeanInstantiationException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vant
 * @since 2018/8/4 13:50
 */
public abstract class StringUtils {
    @NotNull
    public static String firstCharLower(@NotNull String string) {
        char[] buf = new char[string.length()];
        buf[0] = Character.toLowerCase(string.substring(0, 1).charAt(0));
        System.arraycopy(string.substring(1).toCharArray(), 0, buf, 1, string.length() - 1);
        return String.valueOf(buf);
    }

    public static String generateBeanName(@NotNull BeanContainer beanContainer, @NotNull Bean bean, @NotNull Class<?> type) {
        String beanName;
        if ("".equals(bean.value())) {
            beanName = StringUtils.firstCharLower(type.getSimpleName());
            final String old = beanName;
            int i = 1;
            while (beanContainer.nameExist(beanName)) {
                beanName = old + i;
                i++;
            }
        } else {
            beanName = bean.value();
            if (beanContainer.nameExist(beanName)) {
                throw new BeanInstantiationException("the name " + beanName + " is already exist ");
            }
        }
        return beanName;
    }
}
