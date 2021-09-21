package info.wufc.tutorials.javassist.classloader;

import javassist.*;

public class JavassistLoader {
    public static void main(String[] args) throws Throwable {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("D:\\learning\\java\\tutorials\\wufc\\tutorials\\myClassPath");
        Loader cl = new Loader(pool);

        CtClass ct = pool.get("info.wufc.Rectangle");
        ct.setSuperclass(pool.get("info.wufc.Point"));

        Class<?> clazz = cl.loadClass("info.wufc.tutorials.javassist.classloader.Box");
        System.out.println(clazz.getClassLoader());
        assert clazz != Box.class;

        // add listener to javasssit loader
        Translator translator = new Translator() {
            // start 是指使用addTranslator()将translator加入loader时的回调
            @Override
            public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

            }

            // onLoad 是值javassist在load class之前的回调
            // 这里是修改所有的类为public的
            @Override
            public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
                CtClass cc = pool.get(classname);
                cc.setModifiers(Modifier.PUBLIC);
            }
        };
        cl.addTranslator(pool, translator);
        cl.run("info.wufc.javassist.classloader.Box", null);

        Class<?> aClass = cl.loadClass("info.wufc.javassist.classloader.Window");
        CtClass cc = pool.get("info.wufc.javassist.classloader.Rectangle");
        System.out.println(aClass.getClassLoader());
        System.out.println(cc.toClass().getClassLoader());
    }
}
