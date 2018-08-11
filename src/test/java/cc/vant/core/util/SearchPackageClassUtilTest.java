package cc.vant.core.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vant
 * @version 2018/8/3 下午 4:42
 */
class SearchPackageClassUtilTest {

    @Test

    void searchPackageClass() {
        String[] cores = SearchPackageClassUtil.searchPackageClass("cc.vant.core");
        for (String core : cores) {
            System.out.println(core);
        }
    }

    @Test
    void searchPackageClassNotExits() {
        assertThrows(IllegalArgumentException.class, () -> {
            String[] nothings = SearchPackageClassUtil.searchPackageClass("nothing");
            for (String core : nothings) {
                System.out.println(core);
            }
        });
    }

    @Test
    void searchPackageClassWrongType() {
        assertThrows(IllegalArgumentException.class, () -> {
            String[] nothings = SearchPackageClassUtil.searchPackageClass("SearchPackageClassUtilTest.class");
            for (String core : nothings) {
                System.out.println(core);
            }
        });
    }
}