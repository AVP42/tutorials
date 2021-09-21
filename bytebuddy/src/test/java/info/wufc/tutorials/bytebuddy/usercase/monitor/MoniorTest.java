package info.wufc.tutorials.bytebuddy.usercase.monitor;

import info.wufc.tutorials.bytebuddy.Helper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

public class MoniorTest {


    @Test
    public void test() throws Exception {
        DynamicType.Unloaded<BizMethod> unloaded = new ByteBuddy().subclass(BizMethod.class).method(ElementMatchers.named("queryUserInfo"))
                .intercept(MethodDelegation.to(MonitorDemo.class))
                .name("BizMethodEnhancer")
                .make();
        Class<? extends BizMethod> clazz = unloaded.load(getClass().getClassLoader()).getLoaded();
        clazz.getMethod("queryUserInfo", String.class, String.class).invoke(clazz.newInstance(), "10001", "gisgwee");
        Helper.outputClazz(unloaded.getBytes(), "BizMethodEnhancer.class");
    }
}
