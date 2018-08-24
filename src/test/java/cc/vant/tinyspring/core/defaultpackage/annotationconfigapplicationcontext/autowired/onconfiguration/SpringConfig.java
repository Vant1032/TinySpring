package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.onconfiguration;

import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/11 下午 6:35
 */
@Configuration
@ComponentScan(basePackageClasses = {KiwiFruit.class})
public class SpringConfig {
    @Bean
    KiwiFruit getKiwiFruid() {
        final KiwiFruit kiwiFruit = new KiwiFruit();
        kiwiFruit.weight = 1000;
        return kiwiFruit;
    }
}
