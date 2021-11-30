package info.wufc.tutorials.javainstrument;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.TimeUnit;

import info.wufc.tutorials.javainstrument.advice.TimerByteBuddyAdvice;
import info.wufc.tutorials.javainstrument.advice.TimerByteBuddyAdvice3;
import info.wufc.tutorials.javainstrument.advice.TimerByteBuddyConstructAdvice;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import static net.bytebuddy.jar.asm.Opcodes.ACC_PRIVATE;
import static net.bytebuddy.jar.asm.Opcodes.ACC_VOLATILE;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class TimerByteBuddyAgent {

    public static void premain(String args, Instrumentation instrumentation) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader loader = TimerByteBuddyAgent.class.getClassLoader();
        String templateClassName = "info.wufc.tutorials.javainstrument.template.TimerByteBuddyAdviceTemplate";
        String delegatorClassName1 = "info.wufc.tutorials.javainstrument.delegation.AdviceDelegation";
        String delegatorClassName2 = "info.wufc.tutorials.javainstrument.delegation.AdviceDelegation2";

        Class<?> deletator1 = loader.loadClass(delegatorClassName1);
        Class<?> deletator2 = loader.loadClass(delegatorClassName2);

        String internalAdviceName1 =  templateClassName +"_" + deletator1.getSimpleName();
        String internalAdviceName2 =  templateClassName +"_" + deletator2.getSimpleName();
        TypePool typePool = TypePool.Default.of(loader);

        TypeDescription templateTypeDescription = typePool.describe(templateClassName).resolve();
        Class<?> advice1 = new ByteBuddy().redefine(templateTypeDescription, ClassFileLocator.ForClassLoader
                .of(loader))
                .name(internalAdviceName1)
                .field(named("DELEGATION_NAME"))
                .value(delegatorClassName1)
                .make()
                .load(loader).getLoaded();

        Class<?> advice2 = new ByteBuddy().redefine(templateTypeDescription, ClassFileLocator.ForClassLoader
                .of(loader))
                .name(internalAdviceName2)
                .field(named("DELEGATION_NAME"))
                .value(delegatorClassName2)
                .make()
                .load(loader).getLoaded();

        ClassInjector.UsingUnsafe.Factory factory = ClassInjector.UsingUnsafe.Factory.resolve(instrumentation);
        ClassInjector injector = factory.make(loader, null);
//        agentBuilder = agentBuilder.with(new AgentBuilder.InjectionStrategy.UsingUnsafe.OfFactory(factory));


        // 返回的ClassFileTransformer 可以调用reset方法，
        ResettableClassFileTransformer transformer = new AgentBuilder.Default()
                // 声明使用redefine
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                // 大多数vm都要求不能增加或者删除原有的方法
//                .disableClassFormatChanges()
                // debug Instrumentation 过程
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut()).type(ElementMatchers.nameContains("TimerDemo"))
                .transform((builder, typeDescription, classLoader, module) -> {
                    DynamicType.Builder<?> newBuilder = builder
                            .defineField(
                            "_$EnhancedClassField_demo", Object.class, ACC_PRIVATE | ACC_VOLATILE)
                            .implement(EnhancedInstance.class)
                            .intercept(FieldAccessor.ofField("_$EnhancedClassField_demo"))
                            .visit(Advice.to(TimerByteBuddyConstructAdvice.class).on(ElementMatchers.isConstructor()))
                            .visit(Advice.to(advice1).on(ElementMatchers.named("doSomething")))
                            .visit(Advice.to(advice2).on(ElementMatchers.named("doSomethingStatic")));
                    return newBuilder;
                })
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
