package cc.vant.core.util;

/**
 * @author Vant
 * @version 2018/8/4 下午 1:50
 */
public class StringUtils {
    public static String firstCharLower(String string) {
        char[] buf = new char[string.length()];
        buf[0] = Character.toLowerCase(string.substring(0, 1).charAt(0));
        System.arraycopy(string.substring(1).toCharArray(), 0, buf, 1, string.length() - 1);
        return String.valueOf(buf);
    }
}
