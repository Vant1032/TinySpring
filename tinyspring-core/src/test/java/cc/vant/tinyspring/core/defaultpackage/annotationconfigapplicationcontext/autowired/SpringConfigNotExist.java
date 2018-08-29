package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.notexist.CauseWrong;

/**
 * @author Vant
 * @since 2018/8/11 11:58
 */
@Configuration
@ComponentScan(basePackageClasses = {CauseWrong.class})
class SpringConfigNotExist {
}
