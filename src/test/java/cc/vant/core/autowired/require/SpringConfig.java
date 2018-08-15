package cc.vant.core.autowired.require;

import cc.vant.core.annotations.Autowired;
import cc.vant.core.annotations.Bean;
import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;

import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/15 下午 1:10
 */
@Configuration
@ComponentScan(basePackageClasses = {SpringConfig.class})
public class SpringConfig {
    @Bean
    @Autowired
    public Banana getBanana(Map map) {
        return new Banana();
    }

    @Bean
    @Autowired
    public Lemon getLemon(Watermelon watermelon) {
        final Lemon lemon = new Lemon();
        lemon.setWatermelon(watermelon);
        return lemon;
    }
}
