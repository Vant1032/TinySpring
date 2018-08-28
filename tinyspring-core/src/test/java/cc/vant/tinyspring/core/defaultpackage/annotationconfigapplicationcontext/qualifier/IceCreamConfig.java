package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.qualifier;

import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;
import cc.vant.tinyspring.core.annotations.Qualifier;

/**
 * @author Vant
 * @since 2018/8/22 16:21
 */
@Configuration
@ComponentScan(basePackageClasses = {IceCreamConfig.class})
public class IceCreamConfig {
    @Bean
    @Qualifier("cold")
    IceCream getIceCream() {
        return new IceCream();
    }

    @Bean
    @Qualifier("big")
    IceCream getBigIceCream() {
        IceCream iceCream = new IceCream();
        iceCream.setSize(1000);
        return iceCream;
    }

}
