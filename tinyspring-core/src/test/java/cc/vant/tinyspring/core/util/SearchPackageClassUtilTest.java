package cc.vant.tinyspring.core.util;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vant
 * @since 2018/8/3 16:42
 */
class SearchPackageClassUtilTest {
    @Test
    @DisplayName("不存在的包的处理")
    void searchPackageClassNotExits() {
        assertThrows(IllegalArgumentException.class, () -> {
            String[] nothings = SearchPackageClassUtil.searchPackageClass("nothing");
            for (String core : nothings) {
                System.out.println(core);
            }
        });
    }

    @Test
    @DisplayName("包名输错")
    void searchPackageClassWrongType() {
        assertThrows(IllegalArgumentException.class, () -> {
            String[] nothings = SearchPackageClassUtil.searchPackageClass("SearchPackageClassUtilTest.class");
            for (String core : nothings) {
                System.out.println(core);
            }
        });
    }

    private static class Name {
    }

}