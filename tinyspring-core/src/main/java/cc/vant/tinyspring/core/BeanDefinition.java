package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.ScopeType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * @author Vant
 * @since 2018/8/22 16:38
 */
public interface BeanDefinition {
    ScopeType getScopeType();

    void setScopeType(ScopeType scopeType);

    String getBeanName();

    void setBeanName(String beanName);

    boolean isPrimary();

    void setPrimary(boolean primary);

    Class<?> getType();

    void setType(Class<?> clazz);

    String getQualifierString();

    void setQualifierString(String qualifier);

    ArrayList<Annotation> getQualifiers();

    void setQualifiers(ArrayList<Annotation> qualifiers);
}
