package info.wufc.tutorials.javainstrument;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.TimeUnit;

import info.wufc.tutorials.javainstrument.advice.TimerByteBuddyAdvice;
import info.wufc.tutorials.javainstrument.advice.TimerByteBuddyConstructAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class TimerByteBuddyAgent2 {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("[Agent] In premain method");
        new AgentBuilder.Default().with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(ElementMatchers.nameContains("info.wufc"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("doSomething"))
                        .intercept(MethodDelegation.to(new TimerByteBuddyInterceptor())))
                .with(new Listener())
                .installOn(instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("[Agent] In agentmain method");
        // 返回的ClassFileTransformer 可以调用reset方法，
        ResettableClassFileTransformer transformer = new AgentBuilder.Default()
                // 声明使用redefine
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                // 大多数vm都要求不能增加或者删除原有的方法
                .disableClassFormatChanges()
                // debug Instrumentation 过程
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut()).type(ElementMatchers.nameContains("TimerDemo"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .visit(Advice.to(TimerByteBuddyConstructAdvice.class).on(ElementMatchers.isConstructor()))
                        .visit(Advice.to(TimerByteBuddyAdvice.class).on(ElementMatchers.named("doSomething")))
                        .visit(Advice.to(TimerByteBuddyAdvice.class).on(ElementMatchers.named("doSomethingStatic")))

                )
                .with(new Listener())
//                使用delegation方式不再适用
//                .method(ElementMatchers.not(ElementMatchers.isStatic()))
//                .intercept(MethodDelegation.to(TimerByteBuddyInterceptor.class)))
                .installOn(instrumentation);

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        // 重置
//        System.out.println("[Agent] resetting ");
//        transformer.reset(instrumentation, AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);

    }

}
