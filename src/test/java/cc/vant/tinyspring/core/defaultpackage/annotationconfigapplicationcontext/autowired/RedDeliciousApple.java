package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;

/**
 * @author Vant
 * @since 2018/8/9 1:25
 */
@Bean
public class RedDeliciousApple {
    @Autowired
    private Apple apple;

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public Apple getApple() {
        return apple;
    }
}
