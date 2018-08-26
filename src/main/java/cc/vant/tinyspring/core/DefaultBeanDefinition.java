package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.ScopeType;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * @author Vant
 * @since 2018/8/22 17:09
 */
public class DefaultBeanDefinition implements BeanDefinition {
    private ScopeType scopeType = ScopeType.Singleton;
    private String beanName;
    private boolean primary;
    private Class<?> type;
    @Nullable
    private String qualifierString;
    private ArrayList<Annotation> qualifiers;



    @Override
    public ScopeType getScopeType() {
        return scopeType;
    }

    @Override
    public void setScopeType(ScopeType scopeType) {
        this.scopeType = scopeType;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Nullable
    @Override
    public String getQualifierString() {
        return qualifierString;
    }

    @Override
    public void setQualifierString(@Nullable String qualifierString) {
        this.qualifierString = qualifierString;
    }

    @Override
    public ArrayList<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public void setQualifiers(ArrayList<Annotation> qualifiers) {
        this.qualifiers = qualifiers;
    }
}
