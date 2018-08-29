package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.constructor;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Component;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.Apple;

/**
 * @author Vant
 * @since 2018/8/10 23:34
 */
@SuppressWarnings("CanBeFinal")
@Component
public class Pear {

    private Apple apple;

    @Autowired
    public Pear(Apple apple) {
        this.apple = apple;
    }

    public Apple getApple() {
        return apple;
    }
}
