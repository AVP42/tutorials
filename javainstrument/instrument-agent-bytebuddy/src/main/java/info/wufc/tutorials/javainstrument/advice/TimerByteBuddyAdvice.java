package info.wufc.tutorials.javainstrument.advice;

import info.wufc.tutorials.javainstrument.delegation.AdviceDelegation;
import info.wufc.tutorials.javainstrument.MethodContext;
import info.wufc.tutorials.javainstrument.Skip;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;

/**
 * 当需要动态修改时, 通常用使用advice来实现
 */
public class TimerByteBuddyAdvice {
    public static AdviceDelegation delegation = new AdviceDelegation();

    @Advice.OnMethodEnter(skipOn = Skip.class)
    public static Object onEnter(@Advice.This(optional = true) Object obj,
                               @Advice.Origin Method method,
                               @Advice.Origin Class clazz,
                               @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                               @Advice.Local("methodContext") MethodContext context
    ) {
        Object[] _args = args;
        args[0] = 22222;
        context = new MethodContext();
        delegation.before(obj, method, clazz, _args, context);
        if(!context.isContinue()) return Skip.INSTANCE;
        args = _args;
        return  null;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(@Advice.This(optional = true) Object obj,
                              @Advice.Origin Method method,
                              @Advice.Origin Class clazz,
                               @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                               @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret,
                               @Advice.Thrown Throwable throwable,
                               @Advice.Local("methodContext") MethodContext context
    ) throws Throwable {
        if(throwable!=null){
            delegation.afterThrowing(obj, method, clazz, args, ret, throwable, context);
            throw throwable;
        }
        if(!context.isContinue()) {
            ret = context._ret();
        }
        ret = delegation.afterReturning(obj, method, clazz, args, ret, throwable, context);
    }



}
