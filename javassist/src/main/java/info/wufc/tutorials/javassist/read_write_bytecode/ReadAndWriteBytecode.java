package info.wufc.tutorials.javassist.read_write_bytecode;


import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public class ReadAndWriteBytecode {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        // ClassPool javassist用于控制字节码修改， 是一个compile-time class(CtClass)的容器。
        // 底层使用一个hash表，作为缓存，如果不存在则读取class文件
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.Point");
        cc.setSuperclass(pool.get("info.wufc.Rectangle"));
        // 当调用writeFile,对字节码所做的修改，就会体现在硬盘的字节码文件上
        cc.writeFile();
        // 也可以通过toByteCode的方式直接获取到字节码
        byte[] bytecode = cc.toBytecode();
        // 也可以直接load这个ctClass，返回字节码对象
        // 没有传参数的话，会请求当前线程的context class loader 来加载这个字节码文件，
        // context class loader是一个与线程相关的类加载器，类似与ThreadLocal，
        // 每一个线程对应一个上下文类加载器
        Class clazz = cc.toClass();
    }
}
