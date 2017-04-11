package com.keke.superman.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.Nullable;

public final class Predicates2
{
    public static <T extends AnnotatedElement> Predicate<T> isAnnotationPresent(Class<? extends Annotation> annotationClass)
    {
        return new AnnotationPresent(annotationClass);
    }

    public static <T> Predicate<T> propertyPredicateEqualTo(String property, Object value)
    {
        return propertyPredicate(property, Predicates.equalTo(value));
    }

    public static <T, V> Predicate<T> propertyPredicate(String property, Predicate<V> propertyPredicate)
    {
        ValidateUtils.notBlank(property, "创建Bean属性谓词时，属性名'property'不能为空", new Object[0]);
        ValidateUtils.notNull(property, "创建Bean属性谓词时，属性谓词'propertyPredicate'不能为空", new Object[0]);
        return new PropertyPredicate(property, propertyPredicate);
    }

    static class PropertyPredicate<T, V>
            implements Predicate<T>
    {
        private String a;
        private Predicate<V> b;

        PropertyPredicate(String property, Predicate<V> propertyPredicate)
        {
            this.a = property;
            this.b = propertyPredicate;
        }

        public boolean apply(@Nullable T input)
        {
            //TODO
            //return (input != null) && (this.b.apply(Reflections.getFieldValue(input, this.a)));
            return (input != null) && (this.b.apply((V) Reflections.getFieldValue(input, this.a)));
        }
    }

    private static class AnnotationPresent<T extends AnnotatedElement>
            implements Predicate<T>
    {
        private Class<? extends Annotation> a;

        AnnotationPresent(Class<? extends Annotation> annotationClass)
        {
            this.a = annotationClass;
        }

        public boolean apply(T input)
        {
            return input.isAnnotationPresent(this.a);
        }
    }
}

