package info.wufc.tutorials.javainstrument;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TimerJavassistTransformer implements ClassFileTransformer {

    private static final String INTERCEPT_METHOD = "doSomething";

    /** The internal form class name of the class to transform */
    private String targetClassName;
    /** The class loader of the class we want to transform */
    private ClassLoader targetClassLoader;

    public TimerJavassistTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); //replace . with /
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {
            System.out.println("[Agent] Transforming class " + className);
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod(INTERCEPT_METHOD);
                m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = System.currentTimeMillis();");

                StringBuilder endBlock = new StringBuilder();

                m.addLocalVariable("endTime", CtClass.longType);
                m.addLocalVariable("opTime", CtClass.longType);
                endBlock.append("endTime = System.currentTimeMillis();");
                endBlock.append("opTime = (endTime-startTime)/1000;");

                endBlock.append("System.out.println(\"[Application] doSomething operation completed in:\" + opTime + \" seconds!\");");

                m.insertAfter(endBlock.toString());

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return byteCode;
    }
}
