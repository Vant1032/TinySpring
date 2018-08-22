package cc.vant.core;

import cc.vant.core.annotations.ScopeType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * @author Vant
 * @version 2018/8/22 下午 4:38
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
