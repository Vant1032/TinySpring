package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.onconfiguration;

import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @since 2018/8/11 18:35
 */
@Configuration
@ComponentScan(basePackageClasses = {KiwiFruit.class})
public class SpringConfig {
    @Bean
    KiwiFruit getKiwiFruit() {
        final KiwiFruit kiwiFruit = new KiwiFruit();
        kiwiFruit.weight = 1000;
        return kiwiFruit;
    }
}
