package info.wufc.javassist.classloader;

import java.io.*;

public class ClassLoadingInJava {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        // 在java中，多个classloader可以同时存在，每个classLoader创建了自己的空间。
        // 使得不同的class loader可以对于一个class 文件加载成不同的class 类。
        // 这种特性可以使得我们可以在1个jvm中运行多个应用程序，即时这些应用程序使用的是同一份class 文件

        // 需要注意的是 jvm 不允许动态重新加载class，在运行时期，一旦一个class loader 加载了一个class， 不会在重载一个修改过的版本
        // 这意味着我们在jvm load了字节码之后，就不能修改class的定义了。
        // 但是JPDA(Java Platform Debugger Architecture) 提供了一定的重载能力

        // 如果同一份class 文件被不同的class loader加载成两个class， 如果其中一个要指派给另一个的变量时，将会抛出classCastException
        throwClassCastException();

        // the classes that the definition of a class C referes to are loaded by the real loader of C
        // Recall that all the classes referred to in a class C are loaded by the real loader of C



    }

    private static void throwClassCastException() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 自定义classLoader 关键在于覆盖findClass，读取class 文件，然后通过自带的defineClass，生成字节码对象
        ClassLoader myLoader = new ClassLoader(null) {

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {

                try (FileInputStream in = new FileInputStream("D:\\learning\\java\\tutorials\\wufc\\tutorials\\javassist\\target\\classes\\info\\wufc\\javassist\\classloader\\Box.class");
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    int len;
                    while ((len = in.read()) != -1) {
                        out.write(len);
                    }
                    byte[] data = out.toByteArray();
                    return defineClass("info.wufc.javassist.classloader.Box", data, 0, data.length);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return super.findClass(name);
            }
        };
        myLoader.getParent();
        Class<?> clazz = myLoader.loadClass("info.wufc.javassist.classloader.Box");
        Object obj = clazz.newInstance();
        // 由于obj的class 对象不同于 bo的class对象
        // info.wufc.javassist.classloader.Box cannot be cast to info.wufc.javassist.classloader.Box
        Box b0 = (Box) (obj);
    }

}
