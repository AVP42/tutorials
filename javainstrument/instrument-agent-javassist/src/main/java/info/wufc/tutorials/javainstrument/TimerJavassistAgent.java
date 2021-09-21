package info.wufc.tutorials.javainstrument;

import java.lang.instrument.Instrumentation;

public class TimerJavassistAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("[Agent] In premain method");
        String className = "info.wufc.javainstrument.TimerDemo";
        transformClass(className,instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("[Agent] In agentmain method");
        String className = "info.wufc.javainstrument.TimerDemo";
        transformClass(className,instrumentation);
    }


    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (Exception ex) {
            System.out.println("Class [{}] not found with Class.forName");
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        TimerJavassistTransformer dt = new TimerJavassistTransformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }
}
