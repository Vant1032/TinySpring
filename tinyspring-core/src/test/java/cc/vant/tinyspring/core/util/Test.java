package cc.vant.tinyspring.core.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Vant
 * @since 2018/8/26 23:50
 */
public class Test {
    public static void main(String[] args) {

        System.out.println(Arrays.asList());

        for (String arg : args) {
            System.out.println("arg = " + arg);
        }
    }
}
