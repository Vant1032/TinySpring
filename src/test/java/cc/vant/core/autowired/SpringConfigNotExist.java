package cc.vant.core.autowired;

import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.notexist.CauseWrong;

/**
 * @author Vant
 * @version 2018/8/11 上午 11:58
 */
@Configuration
@ComponentScan(basePackageClasses = {CauseWrong.class})
public class SpringConfigNotExist {
}
