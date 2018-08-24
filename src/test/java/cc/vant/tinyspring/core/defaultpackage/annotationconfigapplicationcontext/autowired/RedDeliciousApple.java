package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:25
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
