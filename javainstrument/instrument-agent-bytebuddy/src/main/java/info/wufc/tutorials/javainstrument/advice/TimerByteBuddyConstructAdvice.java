package info.wufc.tutorials.javainstrument.advice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import info.wufc.tutorials.javainstrument.delegation.AdviceConstructDelegation;
import info.wufc.tutorials.javainstrument.delegation.AdviceDelegation;
import info.wufc.tutorials.javainstrument.MethodContext;
import info.wufc.tutorials.javainstrument.Skip;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

/**
 * 当需要动态修改时, 通常用使用advice来实现
 */
public class TimerByteBuddyConstructAdvice {
    public static AdviceConstructDelegation delegation = new AdviceConstructDelegation();


    @Advice.OnMethodExit
    public static void afterConstruct(@Advice.Origin Constructor  method,
                               @Advice.Origin Class clazz,
                               @Advice.AllArguments Object[] args,
                               @Advice.FieldValue(value = "_$EnhancedClassField_vts", readOnly = false) Object field
    ) {
        field = delegation.afterConstruct(method, clazz, args, field);
    }

}
