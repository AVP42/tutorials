package info.wufc.tutorials.javassist.classpool;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ChangingClassName {
    public static void main(String[] args) throws NotFoundException {
        // 一个新的class可以被定义层一个已经存在的类的副本
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.Point");
        CtClass cc1 = pool.get("info.wufc.Point");
        // 仅需要设置名字，这个compile-time class 就是代表info.wufc.Pair的ctClass，原来的不会变化
        cc.setName("info.wufc.Pair");
        CtClass cc2 = pool.get("info.wufc.Pair");
        CtClass cc3 = pool.get("info.wufc.Point");
        assert cc != cc3;
        assert cc1 == cc2;
        // classPool 维护一对一的映射，在同一个classPool中不能有两个ctClass对应一个class file的情况，
        // 果需要这种情况，需要构造两个不同的classPool对象
        // 通过修改不同classPool中的ctClass构造两个不同版本的class1

        // 创建classPool.getDefault()的副本
        ClassPool poolCopied = new ClassPool(true);


    }

}
