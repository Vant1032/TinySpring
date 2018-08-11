package core.autowired;

import core.annotations.ComponentScan;
import core.annotations.Configuration;
import core.notexist.CauseWrong;

/**
 * @author Vant
 * @version 2018/8/11 上午 11:58
 */
@Configuration
@ComponentScan(basePackageClasses = {CauseWrong.class})
public class SpringConfigNotExist {
}
