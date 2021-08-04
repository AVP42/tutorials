package info.wufc.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ByteBuddyTutorials {


    @Test
    public void CreatingClass() {
        // 默认名称是 父类全限定名 + ByteBuddy 后缀 + 随机后缀
        DynamicType.Unloaded<Foo> dynamicType = new ByteBuddy().subclass(Foo.class)
                .make();
        Class<?> clazz = dynamicType.load(getClass().getClassLoader()).getLoaded();
        System.out.println(clazz.getName());

        // 如果父类是java.lang包下面的，则会增加前缀net.bytebuddy.renamed
        // 比如net.bytebuddy.renamed.java.lang.Object$ByteBuddy$avnAeVLu
        Class<?> clazz2 = new ByteBuddy().subclass(Object.class)
                .make().load(getClass().getClassLoader()).getLoaded();
        System.out.println(clazz2.getName());

        // 指定名称
        Class<?> clazz3 = new ByteBuddy().subclass(Object.class).name("example.Type")
                .make().load(getClass().getClassLoader()).getLoaded();
        assertEquals(clazz3.getName(), "example.Type");

    }

    @Test
    public void CreatingClass2() {
        // 自定义命名策略
        Class<?> clazz = new ByteBuddy().with(new NamingStrategy.AbstractBase() {
            @Override
            protected String name(TypeDescription superClass) {
                return "i.love.ByteBuddy." + superClass.getSimpleName();
            }
        }).subclass(Object.class).make().load(getClass().getClassLoader()).getLoaded();
        assertEquals(clazz.getName(), "i.love.ByteBuddy.Object");
    }

    @Test
    public void CreatingClass3() {
        // 修改默认的“ByteBuddy” 后缀
        Class<?> clazz = new ByteBuddy().with(new NamingStrategy.SuffixingRandom("suffix"))
                .subclass(Object.class).make().load(getClass().getClassLoader()).getLoaded();
        System.out.println(clazz.getName()); // net.bytebuddy.renamed.java.lang.Object$suffix$wb8KxX5M

    }

    @Test
    public void dslAndImmutability() {
        // byte buddy 几乎所有的类都是不可变的（immutable） 下面的方式会
        ByteBuddy byteBuddy = new ByteBuddy();
        byteBuddy.with(new NamingStrategy.SuffixingRandom("suffix")); // 返回新的对象，不再是之前的那个byteBuddy
        Class<?> clazz = byteBuddy.subclass(Object.class).make().load(getClass().getClassLoader()).getLoaded();
        System.out.println(clazz.getName());// 不会有suffix后缀
    }

    @Test
    public void redefiningClass() {
        // 利用了jvm的HotSwap特性 （热插拔）
        ByteBuddyAgent.install();
        Foo oldFoo = new Foo();
        new ByteBuddy().redefine(Foo.class).method(ElementMatchers.named("bar"))
                .intercept(FixedValue.value("foo"))
                .make().load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        assertEquals(new Foo().bar(), "foo");
        assertEquals(oldFoo.bar(), "foo");

    }

    @Test
    public void redefiningClass2() {
        // 还可以将Foo类变为Fin
        ByteBuddyAgent.install();
        Foo oldFoo = new Foo();
        new ByteBuddy().redefine(Fin.class)
                .name(Foo.class.getName())
                .make().load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        assertEquals(oldFoo.m(), "fin");
    }

    @Test
    public void rebasingClass() throws IOException {
        // TODO class redefinition failed: attempted to add a method
        // 由于HotSwap要求类的schema一样，所以rebase这种需要增加方法的是不能使用reloadStrategy
        ByteBuddyAgent.install();
        new ByteBuddy().rebase(Foo.class).method(ElementMatchers.named("bar"))
                .intercept(FixedValue.value("foo"))
                .make()
//                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .saveIn(new File(getClass().getResource("/").getPath()));

        System.out.println(new Foo().bar());
    }


    @Test
    public void LoadingClass() throws IOException {
        ClassLoader defaultClassLoader = new ByteBuddy().subclass(Object.class).make()
                .load(getClass().getClassLoader())
                .getLoaded().getClassLoader();
        System.out.println("defaultClassLoader:" + defaultClassLoader);

        ClassLoader wrappedClassLoader = new ByteBuddy().subclass(Object.class).make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded().getClassLoader();
        System.out.println("wrappedClassLoader:" + wrappedClassLoader);

        ClassLoader childFirstClassLoader = new ByteBuddy().subclass(Object.class).make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
                .getLoaded().getClassLoader();
        System.out.println("childFirstClassLoader:" + childFirstClassLoader);

        ClassLoader injectionClassLoader = new ByteBuddy().subclass(Object.class).make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded().getClassLoader();
        System.out.println("injectionClassLoader:" + injectionClassLoader);
    }


    /**
     * TODO Class already loaded: class info.wufc.other.Geo
     */
    @Test
    public void workingWithUnloadedClass() throws NoSuchFieldException {
        TypePool typePool = TypePool.Default.ofSystemLoader();
        new ByteBuddy()
                .redefine(typePool.describe("info.wufc.other.Geo").resolve(),
                        ClassFileLocator.ForClassLoader.ofSystemLoader())
                .defineField("qux", String.class)
                .make()
                .load(ClassLoader.getSystemClassLoader());
//        assertEquals(.getDeclaredField("qux"), notNullValue());
    }


}
