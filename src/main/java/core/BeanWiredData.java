package core;

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
    ArrayList<Field> fields = new ArrayList<>();

    public void add(Field field) {
        this.fields.add(field);
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

}
