package me.niloybiswas.spring_lite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    private Class<?> clazz;
    private Object instance;
    private Method method;
    private MethodType methodType;
    private String url;
}
