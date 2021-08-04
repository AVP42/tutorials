package info.wufc.bytebuddy.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class TimerAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        new AgentBuilder.Default().type(ElementMatchers.nameEndsWith("Timed"))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        return builder.method(ElementMatchers.any()).intercept(MethodDelegation.to(TimingInterceptor.class));
                    }
                }).installOn(instrumentation);
    }
}
