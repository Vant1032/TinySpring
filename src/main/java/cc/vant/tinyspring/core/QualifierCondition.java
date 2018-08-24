package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.Qualifier;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Vant
 * @version 2018/8/24 下午 7:59
 */
public class QualifierCondition {
    private String qualifierStr;
    private Annotation[] qualifiers;

    public QualifierCondition() {
    }

    public QualifierCondition(String qualifierStr, Annotation[] qualifiers) {
        this.qualifierStr = qualifierStr;
        this.qualifiers = qualifiers;
    }

    public String getQualifierStr() {
        return qualifierStr;
    }

    public void setQualifierStr(String qualifierStr) {
        this.qualifierStr = qualifierStr;
    }

    public Annotation[] getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Annotation[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public boolean matchQualifier(BeanDefinition def) {
        return def.getQualifierString().equals(qualifierStr) && def.getQualifiers().containsAll(Arrays.asList(qualifiers));
    }

    /**
     *
     * @param qualifiers 可以是@Qualifier(最多只能有一个),也可以是被@Qualifier注解的其它注解
     * @return QualifierCondition.getQualifierStr 可能为null
     */
    public static QualifierCondition get(Annotation... qualifiers) {
        Annotation[] tmp = null;
        QualifierCondition condition = new QualifierCondition();
        for (int i = 0; i < qualifiers.length; i++) {
            if (qualifiers[i] instanceof Qualifier) {
                Qualifier q = (Qualifier) qualifiers[i];
                condition.setQualifierStr(q.value());
                tmp = new Annotation[qualifiers.length - 1];
                System.arraycopy(qualifiers, 0, tmp, 0, i - 1);
                System.arraycopy(qualifiers, i + 1, tmp, i, qualifiers.length - i - 1);
                condition.setQualifiers(tmp);
                return condition;
            }
        }
        condition.setQualifiers(qualifiers);
        return condition;
    }
}
