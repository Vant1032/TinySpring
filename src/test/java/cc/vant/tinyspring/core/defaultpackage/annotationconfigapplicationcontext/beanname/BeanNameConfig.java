package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.beanname;

import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/18 下午 1:37
 */
@Configuration
public class BeanNameConfig {
    @Bean
    Integer getHundred() {
        return 100;
    }

    @Bean
    Integer getThousand() {
        return 1000;
    }
}