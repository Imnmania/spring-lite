package me.niloybiswas.spring_lite.annotations;

import me.niloybiswas.spring_lite.MethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PutMapping {
    String url() default "";
    MethodType methodType() default MethodType.PUT;
}
