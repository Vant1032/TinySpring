package core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vant
 * @version 2018/8/3 下午 4:42
 */
class SearchPackageClassUtilTest {

    @Test
    void searchPackageClass() {
        String[] cores = SearchPackageClassUtil.searchPackageClass("core");
        for (String core : cores) {
            System.out.println(core);
        }
    }
}