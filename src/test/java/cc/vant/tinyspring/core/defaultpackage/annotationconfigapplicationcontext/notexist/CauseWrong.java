package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.notexist;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;

import java.util.Scanner;

/**
 * @author Vant
 * @since 2018/8/10 21:52
 */
@Bean
public class CauseWrong {
    @Autowired
    private Scanner scanner;

    public Scanner getScanner() {
        return scanner;
    }
}
