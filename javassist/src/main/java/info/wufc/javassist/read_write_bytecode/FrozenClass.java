package info.wufc.javassist.read_write_bytecode;

import javassist.*;

import java.io.IOException;

public class FrozenClass {
    public static void main(String[] args) throws NotFoundException, IOException, CannotCompileException {
        // 当使用writeFile(), toClass(), toByteCode() 将CtClass转换成class file之后，不再允许对CtClass的修改
        // 因为jvm对于已经loading的字节码，不允许再次loading， 所以有这个限制作为一种警告
        // 如果需要解冻defrost， 可以使用defrost()方法
        ClassPool pool = ClassPool.getDefault();

        CtClass cc = pool.get("info.wufc.Point");
        cc.addInterface(pool.makeInterface("info.wufc.Rectangle3"));
        // writeFile之后 查看下面的路径就可以看到改动, 如果写的是不同的路径
        cc.writeFile();
//        cc.writeFile("D:\\learning\\java\\tutorials\\wufc\\tutorials\\javassist\\target\\classes\\info\\wufc\\b");
        try {
            // 已经被冻结，会提示info.wufc.Point class is frozen
            cc.addField(CtField.make("private int z;", cc));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 使用解冻; 如果被prune了，也不能解冻
        cc.defrost();
        cc.addField(CtField.make("private int z;", cc));

    }
}
