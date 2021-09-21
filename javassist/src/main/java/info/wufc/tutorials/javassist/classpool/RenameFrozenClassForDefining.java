package info.wufc.tutorials.javassist.classpool;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public class RenameFrozenClassForDefining {

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.Point");
        cc.writeFile();
        try {
            // 已经被冻结的cc不能通过setName复制ctClass
            cc.setName("info.wufc.Pair");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 为了避免这种情况，可以使用getAndRename
        CtClass ctRenamed = pool.getAndRename("info.wufc.Point", "info.wufc.Pair");
        ctRenamed.writeFile();

    }
}
