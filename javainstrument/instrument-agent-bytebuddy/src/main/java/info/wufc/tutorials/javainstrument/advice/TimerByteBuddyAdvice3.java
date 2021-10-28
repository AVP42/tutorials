package info.wufc.tutorials.javainstrument.advice;

import java.lang.reflect.Method;

import info.wufc.tutorials.javainstrument.MethodContext;
import info.wufc.tutorials.javainstrument.Skip;
import info.wufc.tutorials.javainstrument.delegation.AdviceDelegation;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

/**
 * 当需要动态修改时, 通常用使用advice来实现
 */
public class TimerByteBuddyAdvice3 {
    public static AdviceDelegation delegation = new AdviceDelegation();

    @Advice.OnMethodEnter(skipOn = Skip.class, inline = false)
    public static Object onEnter(@Advice.This(optional = true) Object obj

    ) {
        System.out.println("TimerByteBuddyAdvice3.onEnter");
        return null;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class, inline = false)
    public static void onExit(@Advice.This(optional = true) Object obj

    ) throws Throwable {
        System.out.println("TimerByteBuddyAdvice3.onExit");
    }



}
