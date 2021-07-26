package info.wufc.javassist.classloader;

import javassist.*;
import org.apache.catalina.Session;

import javax.servlet.Servlet;
import javax.servlet.http.HttpSession;

public class ToClassMethod {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        correct();
        error();
        // 对于web server， 使用默认的context class loader是不合适的，这种情况下，会有classCastException
        // 为了避免这种情况，我们需要给toClass传递一个合适的class loader
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("javax.servlet.ServletContext");
        cc.toClass(Session.class.getClassLoader(), Session.class.getProtectionDomain());
    }

    private static void error() throws NotFoundException, CannotCompileException {
        // correct方法中可以正确执行，是因为Hello类在调用toClass之前还没有被加载过
        // 如果jvm已经提前加载了，那么再次调用toClass方法进行loading class时就会报linkError错误 attempted  duplicate class definition
        // 因为一个classloader不能加载两个版本的class
        Hello.loaded = true;
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.javassist.classloader.ToClassMethod$Hello");
        try {
            cc.toClass();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void correct() throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
        // 如果知道特定的类名，我们可以通过ClassPool对象的get方法，获取到该类字节码，进行修改
        // 如果我们不知道具体的类名，我们可以通过结合classLoader在load时间过程修改字节码
        // javassist提供了这样的class loader，我们也可以自定义class loader
        // toClass 就是使用上下文class loader（context class loader）来load 字节码，从而生成ctClass对象
        // 使用这个方法，调用者需要有适当的权限，否则会报出SecurityException
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("info.wufc.javassist.classloader.ToClassMethod$Hello");
        CtMethod m = cc.getDeclaredMethod("sayHello");
        // 需要使用代码块的方式插入，不能直接写语句！！！
        m.insertBefore("{System.out.println(\"Hello.say():\");}");
        Class clazz = cc.toClass();
        ((Hello) clazz.newInstance()).sayHello();
    }

    static class Hello{
        static boolean loaded;
        public void sayHello() {
            System.out.println("Hello");

        }
    }
}
