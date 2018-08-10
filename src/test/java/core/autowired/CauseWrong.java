package core.autowired;

import core.annotations.Autowired;
import core.annotations.Bean;

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
