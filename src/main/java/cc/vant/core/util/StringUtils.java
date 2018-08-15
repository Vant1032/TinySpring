package cc.vant.core.util;

import cc.vant.core.BeanContainer;
import cc.vant.core.annotations.Bean;
import cc.vant.core.exception.BeanInstantiationException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vant
 * @version 2018/8/4 下午 1:50
 */
public class StringUtils {
    @NotNull
    public static String firstCharLower(@NotNull String string) {
        char[] buf = new char[string.length()];
        buf[0] = Character.toLowerCase(string.substring(0, 1).charAt(0));
        System.arraycopy(string.substring(1).toCharArray(), 0, buf, 1, string.length() - 1);
        return String.valueOf(buf);
    }

    public static String generateBeanName(@NotNull Bean bean, @NotNull Class<?> type) {
        String beanName;
        if ("".equals(bean.value())) {
            beanName = StringUtils.firstCharLower(type.getSimpleName());
            final String old = beanName;
            int i = 1;
            while (BeanContainer.nameExist(beanName)) {
                beanName = old + i;
                i++;
            }
        } else {
            beanName = bean.value();
            if (BeanContainer.nameExist(beanName)) {
                throw new BeanInstantiationException("the name " + beanName + " is already exist ");
            }
        }
        return beanName;
    }
}
