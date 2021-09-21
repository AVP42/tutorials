package info.wufc.tutorials.javassist.read_write_bytecode;

import info.wufc.tutorials.Point;
import javassist.*;

import java.io.IOException;

public class PruningClass {
    public static void main(String[] args) throws NotFoundException, IOException, CannotCompileException, IllegalAccessException, InstantiationException {
        // 为了减少内存占用，可以通过ClassPool.doPruning设置如果ctClass已经下沉到class flle中，那么可以丢弃一些内容，比如方法体等，
        // 默认是false， 如果为true，我们也可以针对特定的ctClass不采用修剪prune
        ClassPool pool = ClassPool.getDefault();
        ClassPool.doPruning = true;

        CtClass cc = pool.get("info.wufc.Point");
        CtMethod method = cc.getDeclaredMethod("getX");

        // debugWriteFile 不会被prune， 不会被froze
//        cc.debugWriteFile("D:\\learning\\java\\tutorials\\wufc\\tutorials\\javassist\\target\\classes\\info\\wufc\\a");

        // 可以正常插入代码
        method.insertBefore("System.out.println('this coming out means not being pruned');");
        cc.addMethod(method);

        Class clazz = cc.toClass();
        Point point = (Point) clazz.newInstance();
        point.getX();

        // 模拟裁剪掉，如果调用了writeFile(), toClass(), toByteCode() 也会被裁剪(设置了doPruning=true), 且会被冻结
        cc.prune();
        try {
            method.insertBefore("System.out.println('this coming out means not being pruned');");
            //
            System.out.println( method.getSignature());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
