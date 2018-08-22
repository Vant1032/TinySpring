package cc.vant.core.defaultpackage.annotationconfigapplicationcontext.qualifier;

import cc.vant.core.annotations.Bean;
import cc.vant.core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/22 下午 4:21
 */
@Configuration
public class IceCreamConfig {
    @Bean
    IceCream getIceCream() {
        return new IceCream();
    }
}
