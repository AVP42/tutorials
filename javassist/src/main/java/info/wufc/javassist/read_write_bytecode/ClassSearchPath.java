package info.wufc.javassist.read_write_bytecode;

import javassist.*;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ClassSearchPath {

    public static void main(String[] args) throws NotFoundException, IOException {
        // ClassPool.getDefault 只会加载在jvm下面的classPath； 如果是运行web server，比如jboss或者tomcat
        // 这种web server 除了会使用system class loader， 还会使用其他多个classLoader。
        // 这种情况下，就必须添加classPath到classPool。
        ClassPool pool = ClassPool.getDefault();
        // 1.使用classClasspath 下面这种方式是注册一个用于加载ServletContext类的classpath
        pool.insertClassPath(new ClassClassPath(ServletContext.class));
        // 2.使用urlClassPath
        pool.insertClassPath(new URLClassPath("www.javassist.org", 80, "/java/", "org.javassist."));
        // 3.使用byteArrayClasspath，直接将一个类的二进制字节码传入， 通过get(className)获取即可
        pool.insertClassPath(new ByteArrayClassPath("info.wufc.ByteArrayClasspathDemo", null));
        pool.get("info.wufc.ByteArrayClasspathDemo");
        // 4.使用inputstream，字节码流生成ctClass
        pool.makeClass(new FileInputStream("/Point"));
        // 5.使用自定义classpath
        pool.insertClassPath(new ClassPath() {
            @Override
            public InputStream openClassfile(String classname) throws NotFoundException {
                return null;
            }

            @Override
            public URL find(String classname) {
                return null;
            }

            @Override
            public void close() {

            }
        });

    }

}
