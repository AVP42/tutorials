package info.wufc.tutorials.bytebuddy.quickstart;

import info.wufc.tutorials.bytebuddy.Helper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QuickStart {

    @Test
    public void testGreeting() throws IllegalAccessException, InstantiationException {
        DynamicType.Unloaded<Function> unloaded = new ByteBuddy().subclass(Function.class).method(ElementMatchers.named("apply"))
                .intercept(MethodDelegation.to(new GreetingInterceptor()))
                .make();

        Helper.outputClazz(unloaded.getBytes(), "GreetingFunctional.class");

        Class<? extends Function> dynamicType = unloaded.load(getClass().getClassLoader()).getLoaded();
        assertThat((String) dynamicType.newInstance().apply("Byte Buddy"), is("Hello from Byte Buddy"));
    }
}
