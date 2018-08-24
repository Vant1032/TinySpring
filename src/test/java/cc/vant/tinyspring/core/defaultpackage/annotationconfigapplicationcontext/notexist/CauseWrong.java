package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.notexist;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;

import java.util.Scanner;

/**
 * @author Vant
 * @version 2018/8/10 下午 9:52
 */
@Bean
public class CauseWrong {
    @Autowired
    Scanner scanner;
}
