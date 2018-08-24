package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.r2;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

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
    public Cake getCake(Map map) {
        final Cake cake = new Cake();
        cake.setMap(map);
        return cake;
    }
}
