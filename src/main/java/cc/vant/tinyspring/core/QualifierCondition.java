package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.Qualifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Vant
 * @since 2018/8/24 19:59
 */
public class QualifierCondition {
    @Nullable
    private String qualifierStr;
    @NotNull
    private Annotation[] qualifiers = {};

    public QualifierCondition() {
    }

    public QualifierCondition(@Nullable String qualifierStr, @NotNull Annotation[] qualifiers) {
        this.qualifierStr = qualifierStr;
        this.qualifiers = qualifiers;
    }

    @Nullable
    public String getQualifierStr() {
        return qualifierStr;
    }

    public void setQualifierStr(@Nullable String qualifierStr) {
        this.qualifierStr = qualifierStr;
    }

    public Annotation[] getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(@NotNull Annotation[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public boolean matchQualifier(BeanDefinition def) {
        if (qualifierStr != null){
            if (!qualifierStr.equals(def.getQualifierString())) {
                return false;
            }
        }
        return def.getQualifiers().containsAll(Arrays.asList(qualifiers));
    }

    /**
     * 从一堆包含Qualifier的注解中构建QualifierCondition
     */
    public static QualifierCondition get(@NotNull Annotation[] containQualifier) {
        QualifierCondition result = new QualifierCondition();
        ArrayList<Annotation> annotations = new ArrayList<>();
        for (Annotation annotation : containQualifier) {
            if (annotation.getClass().isAnnotationPresent(Qualifier.class)) {
                annotations.add(annotation);
            } else if (annotation instanceof Qualifier) {
                result.setQualifierStr(((Qualifier) annotation).value());
            }
        }
        result.setQualifiers(annotations.toArray(new Annotation[]{}));
        return result;
    }

    public boolean empty() {
        return qualifierStr == null && qualifiers.length == 0;
    }
}
