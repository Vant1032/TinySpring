package cc.vant.tinyspring.core;

import java.lang.annotation.Annotation;

/**
 * @author Vant
 * @version 2018/8/25 下午 9:08
 */
public interface QualifiableBeanFactory extends BeanFactory {
    <T> T getBeanByQualifier(Class<T> requireType, Annotation... qualifiers);
    <T> T getBeanByQualifier(Class<T> requireType, QualifierCondition condition);
}