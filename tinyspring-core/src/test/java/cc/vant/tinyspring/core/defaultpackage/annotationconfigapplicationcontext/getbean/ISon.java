package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.getbean;

import cc.vant.tinyspring.core.annotations.Component;

/**
 * @author Vant
 * @since 2018/8/15 12:37
 */
@Component
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
