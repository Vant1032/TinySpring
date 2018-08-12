package cc.vant.core.util;

/**
 * @author Vant
 * @version 2018/8/12 下午 12:12
 */
public class Three<F, S, T> {
    private F first;
    private S second;
    private T three;

    public Three(F first, S second, T three) {
        this.first = first;
        this.second = second;
        this.three = three;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public T getThree() {
        return three;
    }

    public void setThree(T three) {
        this.three = three;
    }
}
