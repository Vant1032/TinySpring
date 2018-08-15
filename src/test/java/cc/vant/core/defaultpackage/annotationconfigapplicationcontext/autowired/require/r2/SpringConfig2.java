package cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.r2;

import cc.vant.core.annotations.Autowired;
import cc.vant.core.annotations.Bean;
import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.Banana;

import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/15 下午 1:46
 */
@Configuration
@ComponentScan(basePackageClasses = {SpringConfig2.class})
public class SpringConfig2 {

    @Bean
    @Autowired(required = false)
    public Banana getBanana(Map map) {
        final Banana banana = new Banana();
        banana.setMap(map);
        return banana;
    }
}
