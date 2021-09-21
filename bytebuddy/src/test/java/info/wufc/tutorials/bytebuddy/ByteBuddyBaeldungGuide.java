package info.wufc.tutorials.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ByteBuddyBaeldungGuide {

    @Test public void creatJavaClassAtRunTime() throws IllegalAccessException, InstantiationException {
        DynamicType.Unloaded<Object> unloadedType =
            new ByteBuddy().subclass(Object.class).method(ElementMatchers.isToString())
                .intercept(FixedValue.value("Hello world ByteBuddy!")).make();
        DynamicType.Loaded<Object> loadedType = unloadedType.load(getClass().getClassLoader());
        Class<?> dynamicType = loadedType.getLoaded();
        assertEquals(dynamicType.newInstance().toString(), "Hello world ByteBuddy!");
    }

    /**
     * 使用byteBuddy的DSL(domain-specific language 特定领域预研) 实现方法委派
     */
    @Test public void methodDelegationAndCustomLogic() throws IllegalAccessException, InstantiationException {
        String greetings = new ByteBuddy().subclass(Foo.class).method(
            ElementMatchers.named("sayHelloFoo").and(ElementMatchers.isDeclaredBy(Foo.class))
                .and(ElementMatchers.returns(String.class))).intercept(MethodDelegation.to(Bar.class)).make()
            .load(getClass().getClassLoader()).getLoaded().newInstance().sayHelloFoo();
        assertEquals(greetings, Bar.sayHelloBar());
    }

    @Test public void MethodAndFiledDefinition() throws Exception {
        Class<?> clazz = new ByteBuddy().subclass(Object.class).name("MyClassName")
            .defineMethod("custom", String.class, Modifier.PUBLIC).intercept(MethodDelegation.to(Bar.class))
            .defineField("x", String.class, Modifier.PUBLIC).make()
            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
        Method m = clazz.getDeclaredMethod("custom", null);
        assertEquals(m.invoke(clazz.newInstance()), Bar.sayHelloBar());
        assertNotNull(clazz.getDeclaredField("x"));
    }

    @Test public void RedefiningExistingClass() {
        Instrumentation install = ByteBuddyAgent.install();
        new ByteBuddy().redefine(Foo.class).method(ElementMatchers.named("sayHelloFoo"))
            .intercept(FixedValue.value("Hello Foo Redefined")).make()
            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        Foo foo = new Foo();
        assertEquals(foo.sayHelloFoo(), "Hello Foo Redefined");
    }

}
