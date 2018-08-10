package core.autowired.constructor;

import core.annotations.Autowired;
import core.annotations.Bean;
import core.autowired.Apple;

/**
 * @author Vant
 * @version 2018/8/10 下午 11:34
 */
@Bean
public class Pear {

    private Apple apple;

    public Pear() {
    }

    @Autowired
    public Pear(Apple apple) {
        this.apple = apple;
    }

    public Apple getApple() {
        return apple;
    }
}
