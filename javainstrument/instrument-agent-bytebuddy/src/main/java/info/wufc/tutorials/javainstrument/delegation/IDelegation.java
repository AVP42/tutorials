package info.wufc.tutorials.javainstrument.delegation;

import info.wufc.tutorials.javainstrument.MethodContext;

import java.lang.reflect.Method;

public interface IDelegation {
    void before(Object obj, Method method, Class clazz, Object[] args, MethodContext context);

    Object afterReturning(Object obj, Method method, Class clazz, Object[] args, Object ret, Throwable throwable, MethodContext context);

    void afterThrowing(Object obj, Method method, Class clazz, Object[] args, Object ret, Throwable throwable, MethodContext context);
}

