package cc.vant.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Vant
 * @version 2018/8/9 下午 8:20
 */
public class BeanWiredData {
    /**
     * 需要autowired的field
     */
    private ArrayList<Field> fields = new ArrayList<>();
    private Constructor constructor;

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public void add(Field field) {
        this.fields.add(field);
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

}
