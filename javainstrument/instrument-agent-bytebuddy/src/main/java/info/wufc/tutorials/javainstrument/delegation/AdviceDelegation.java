package info.wufc.tutorials.javainstrument.delegation;

import info.wufc.tutorials.javainstrument.MethodContext;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AdviceDelegation implements IDelegation{
    public void before(Object obj, Method method, Class clazz, Object[] args, MethodContext context){
        args[0] = 2000;
        System.out.println(">>>>>>>>>>>before>>>>>");
        System.out.println("obj: " + obj);
        System.out.println("method: " +method);
        System.out.println("clazz:" +clazz);
        System.out.println("args:" + Arrays.toString(args));
        System.out.println("context:" +context);
        context.setContext(System.currentTimeMillis());
    }

    public Object afterReturning(Object obj, Method method, Class clazz, Object[] args, Object ret, Throwable throwable, MethodContext context) {
        System.out.println(">>>>>>>>afterReturning>>>>>>>>");
        System.out.println("obj: " + obj);
        System.out.println("method: " +method);
        System.out.println("clazz:" +clazz);
        System.out.println("args:" + Arrays.toString(args));
        System.out.println("context:" +context);
        System.out.println("ret:" + ret);
        System.out.println("throwable:" + throwable);
        System.out.println(System.currentTimeMillis() - (long)context.getContext());
        return 100;
    }

    public void afterThrowing(Object obj, Method method, Class clazz, Object[] args, Object ret, Throwable throwable, MethodContext context) {
        System.out.println(">>>>>>>>afterThrowing>>>>>>>>");
        System.out.println("obj: " + obj);
        System.out.println("method: " +method);
        System.out.println("clazz:" +clazz);
        System.out.println("args:" + Arrays.toString(args));
        System.out.println("context:" +context);
        System.out.println("ret:" + ret);
        System.out.println("throwable:" + throwable);
        System.out.println(System.currentTimeMillis() - (long)context.getContext());
        throwable.addSuppressed(new RuntimeException("haha"));
    }
}
