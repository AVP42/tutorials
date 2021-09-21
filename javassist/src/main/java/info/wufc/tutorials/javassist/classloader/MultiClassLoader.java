package info.wufc.tutorials.javassist.classloader;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MultiClassLoader {
    public static void main(String[] args) throws Exception {
        // 多个classloader 形成树状结构，每个classLoad 除了Bootstrap class loader 外均有一个parent，由于双亲委派机制，
        // 一个类被加载可以分为触发加载器和真正加载器
        // 如果一个类中需要委派到真正的加载器去执行，那么后续这个类中的其他引用到的class，都将由这个真正的加载器去触发且真正执行。

        ClassLoader myLoader = new ClassLoader() {

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                FileInputStream in = null;
                try (
                        ByteArrayOutputStream out = new ByteArrayOutputStream()
                ) {
                    if(name.contains("Window")){
                        in = new FileInputStream("D:\\learning\\java\\tutorials\\wufc\\tutorials\\myClassPath\\info\\wufc\\javassist\\classloader\\Window.class");
                    } else if (name.contains("Rectangle")) {
                        in = new FileInputStream("D:\\learning\\java\\tutorials\\wufc\\tutorials\\myClassPath\\info\\wufc\\javassist\\classloader\\Rectangle.class");
                    } else if (name.contains("Point")) {
                        in = new FileInputStream("D:\\learning\\java\\tutorials\\wufc\\tutorials\\myClassPath\\info\\wufc\\javassist\\classloader\\Point.class");
                    }
                    int pos;
                    while ((pos = in.read()) != -1) {
                        out.write(pos);
                    }
                    byte[] data = out.toByteArray();
                    return defineClass(name, data, 0, data.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {

                        }
                    }
                }
                return super.findClass(name);
            }
        };
        Class<?> clazz = myLoader.loadClass("info.wufc.javassist.classloader.Window");
        System.out.println(Thread.currentThread().getContextClassLoader()); // AppClassLoader
        System.out.println(clazz.getClassLoader());
        String s = clazz.newInstance().toString();


        // window 的触发loader(initiator loader) 是myLoader, real loader 是AppClassLoader
        // Box的initiator loader和real loader都是AppClassLoader
        // Point的initiator loader和real loader都是AppClassLoader
    }
}
