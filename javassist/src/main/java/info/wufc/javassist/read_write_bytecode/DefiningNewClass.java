package info.wufc.javassist.read_write_bytecode;

import javassist.*;

public class DefiningNewClass {

    public static void main(String[] args) throws CannotCompileException, NotFoundException, IllegalAccessException, InstantiationException {
        // 获取compile-time class所在的容器ClassPool
        ClassPool pool = ClassPool.getDefault();

        // 定义一个新的interface
        CtClass rectangleCc = pool.makeInterface("info.wufc.Rectangle2");
        CtMethod abm = CtNewMethod.abstractMethod(CtPrimitiveType.intType, "getX", null, null, rectangleCc);
        rectangleCc.addMethod(abm);


        // 定义一个新的ctClass，使用classPool.makeClass();
        CtClass pointCc = pool.makeClass("info.wufc.point2");
        // 构建构造器
        CtConstructor constructor = CtNewConstructor.defaultConstructor(pointCc);
        // 构建属性
        CtField field = CtField.make("private int x;", pointCc);
        // 使用ctNewMethod的工厂方法构造更加方便，比起CtMethod
        CtMethod method = CtNewMethod.getter("getX", field);

        pointCc.addInterface(rectangleCc);
        pointCc.addConstructor(constructor);
        pointCc.addField(field);
        pointCc.addMethod(method);
        Class clazz = pointCc.toClass();
        Object o = clazz.newInstance();

        System.out.println(clazz.toGenericString());
    }
}
