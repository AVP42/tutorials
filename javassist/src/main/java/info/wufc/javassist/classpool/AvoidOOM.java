package info.wufc.javassist.classpool;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class AvoidOOM {
    public static void main(String[] args) throws NotFoundException {
        // classpool 永久持有ctClass对象，以满足后续的编译需求。这可能导致oom问题，
        // 一方面，可以使用detach(),将ctClass从pool中移除
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.Point");
        System.out.println(cc.getClassFile().getName()); // info.wufc.Point
        cc.detach();
        // detach之后不能再调用ctclass的方法, 应当重新使用pool.get()来获取
        cc.addInterface(pool.makeInterface("info.wufc.Rectangle4"));

        // 另一方面，使用执行创建的pool，这样就可以被垃圾回收器回收
        // ClassPool.getDefault() 是一个单例模式
        // true 表示追加系统的classpath，相当于appendSystemPath
        ClassPool myPool = new ClassPool(true);


    }
}
