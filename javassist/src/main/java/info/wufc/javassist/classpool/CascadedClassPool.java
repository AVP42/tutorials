package info.wufc.javassist.classpool;

import javassist.ClassPool;
import javassist.NotFoundException;

public class CascadedClassPool {
    public static void main(String[] args) throws NotFoundException {
        // 如果引用是运行在web server中的，就有必要使用多个classPool实例，
        // 每一个classPool对应一个classLoader，也就是一个容器container
        // 多个classPool可以像classLoader级联的方式, 查找方式是先交给parent进行查找，找不到再到child声明的classpath中去寻找
        ClassPool parent = ClassPool.getDefault();
        ClassPool child = new ClassPool(parent);
        child.insertClassPath("./classes");
        // 还可以通过设置child.childFirstLookup，使得先从child中寻找，然后再到parent中寻找
        child.childFirstLookup = true;
    }
}
