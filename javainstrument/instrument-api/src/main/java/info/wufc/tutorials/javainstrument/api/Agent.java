package info.wufc.tutorials.javainstrument.api;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class Agent {

    static byte[] beforeT1FirstLoad;
    static byte[] afterT1FirstLoad;

    static byte[] beforeT1Redefine;
    static byte[] afterT1Redefine;

    static byte[] beforeT1Retransform;
    static byte[] afterT1Retransform;

    static byte[] beforeT2FirstLoad;
    static byte[] afterT2FirstLoad;

    static byte[] beforeT2Redefine;
    static byte[] afterT2Redefine;

    static byte[] beforeT2Retransform;
    static byte[] afterT2Retransform;

    static class Transformer1 implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className.contains("Foo")) {
                System.out.println("Transformer1.transform byteCode:" + classfileBuffer + " size:" + classfileBuffer.length);
                byte[] byteCode = classfileBuffer;
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        CtClass cc = cp.get("info.wufc.tutorials.javainstrument.api.Foo");
                        CtMethod m = cc.getDeclaredMethod("m1");
                        StringBuilder endBlock = new StringBuilder();
                        endBlock.append("System.out.println(\"transform1 foo.m1\");");
                        m.insertAfter(endBlock.toString());
                        byteCode = cc.toBytecode();
                        cc.defrost();
//                        cc.detach();
                    } catch (NotFoundException | CannotCompileException | IOException e) {
                        e.printStackTrace();
                    }
                return byteCode;
            }
            return classfileBuffer;
        }
    }

    static class Transformer2 implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className.contains("Foo")) {
                System.out.println("Transformer2.transform byteCode:" + classfileBuffer + " size:" + classfileBuffer.length);
                byte[] byteCode = classfileBuffer;
                try {
                    ClassPool cp = ClassPool.getDefault();
                    CtClass cc = cp.get("info.wufc.tutorials.javainstrument.api.Foo");
                    CtMethod m = cc.getDeclaredMethod("m2");
                    StringBuilder endBlock = new StringBuilder();
                    endBlock.append("System.out.println(\"transform2 foo.m2\");");
                    m.insertAfter(endBlock.toString());
                    byteCode = cc.toBytecode();
                    cc.defrost();
//                    cc.detach(); // detach 之后再获取时从class file中读取，所以之前的不见了
                } catch (NotFoundException | CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
                return byteCode;
            }
            return classfileBuffer;
        }
    }

    static class Transformer3 implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className.contains("Foo")) {
                System.out.println("Transformer3.transform byteCode:" + classfileBuffer + " size:" + classfileBuffer.length);
                return classfileBuffer;
            }
            return classfileBuffer;
        }
    }

    public static void premain(String args, Instrumentation instrumentation) throws Exception {

        System.out.println("[Agent] In premain method");
        Transformer1 t1 = new Transformer1();
        Transformer2 t2 = new Transformer2();
        Transformer3 t3 = new Transformer3();
        instrumentation.addTransformer(t1, false);
        instrumentation.addTransformer(t2, true);


        Class<?> clazz = Class.forName("info.wufc.tutorials.javainstrument.api.Foo");
        Object obj = clazz.newInstance();
        System.out.println(clazz.getDeclaredMethod("m1").invoke(obj));
        System.out.println(clazz.getDeclaredMethod("m2").invoke(obj));

        System.out.println(clazz.equals(Foo.class));

//
//        byte[] newBytes = new ByteBuddy().redefine(clazz)
//                .method(ElementMatchers.named("m1")).intercept(FixedValue.value("new foo.m1"))
//                .method(ElementMatchers.named("m2")).intercept(FixedValue.value("new foo.m2"))
//                .make().getBytes();
//
//        System.out.println("--------redefine--------");
//        instrumentation.redefineClasses(new ClassDefinition(clazz, newBytes));
//
//        System.out.println(clazz.equals(Foo.class));
//        Object obj2 = clazz.newInstance();
//        System.out.println(clazz.getDeclaredMethod("m1").invoke(obj2));
//        System.out.println(clazz.getDeclaredMethod("m2").invoke(obj2));

        System.out.println("--------retransform--------");
        instrumentation.removeTransformer(t2);
        instrumentation.addTransformer(t3, true);
        instrumentation.retransformClasses(clazz);

        System.out.println(clazz.equals(Foo.class));
        Object obj3 = clazz.newInstance();
        System.out.println(clazz.getDeclaredMethod("m1").invoke(obj3));
        System.out.println(clazz.getDeclaredMethod("m2").invoke(obj3));

    }


}
