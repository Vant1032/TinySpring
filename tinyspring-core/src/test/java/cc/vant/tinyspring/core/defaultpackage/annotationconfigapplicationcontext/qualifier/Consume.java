package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.qualifier;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Component;
import cc.vant.tinyspring.core.annotations.Qualifier;

/**
 * @author Vant
 * @since 2018/8/26 18:00
 */
@Component
class Consume {
    @Autowired
    @Qualifier("big")
    private IceCream iceCream;

    public IceCream getIceCream() {
        return iceCream;
    }
}
