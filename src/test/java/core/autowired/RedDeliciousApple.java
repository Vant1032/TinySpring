package core.autowired;

import core.annotations.Autowired;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:25
 */
public class RedDeliciousApple {
    @Autowired
    private Apple apple;
    public RedDeliciousApple() {
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }
}
