package cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.notexist.CauseWrong;

/**
 * @author Vant
 * @version 2018/8/11 上午 11:58
 */
@Configuration
@ComponentScan(basePackageClasses = {CauseWrong.class})
public class SpringConfigNotExist {
}
