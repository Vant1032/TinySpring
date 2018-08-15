package cc.vant.core.autowired.require.r3;

import cc.vant.core.annotations.Autowired;
import cc.vant.core.annotations.Bean;
import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.autowired.require.Lemon;

/**
 * @author Vant
 * @version 2018/8/15 下午 4:36
 */
@Configuration
@ComponentScan(basePackageClasses = SpringConfig3.class)
public class SpringConfig3 {
    @Bean
    @Autowired
    public Lemon getLemon(Watermelon watermelon) {
        final Lemon lemon = new Lemon();
        lemon.setWatermelon(watermelon);
        return lemon;
    }
}
