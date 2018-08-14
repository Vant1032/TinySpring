package cc.vant.core.getbean;

import cc.vant.core.annotations.Bean;

/**
 * @author Vant
 * @version 2018/8/15 上午 12:37
 */
@Bean
public class ISon extends AFather implements IFather {

    @Override
    public void foo() {
        System.out.println("i son");
    }

    @Override
    public void bar() {
        System.out.println(" bar ");
    }
}
