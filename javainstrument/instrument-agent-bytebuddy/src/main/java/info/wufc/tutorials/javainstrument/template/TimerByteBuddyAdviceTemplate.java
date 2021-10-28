package info.wufc.tutorials.javainstrument.template;

import java.lang.reflect.Method;

import info.wufc.tutorials.javainstrument.MethodContext;
import info.wufc.tutorials.javainstrument.Skip;
import info.wufc.tutorials.javainstrument.delegation.AdviceDelegation;
import info.wufc.tutorials.javainstrument.delegation.IDelegation;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

/**
 * 当需要动态修改时, 通常用使用advice来实现
 */
public class TimerByteBuddyAdviceTemplate {
    public static String DELEGATION_NAME;
    public static IDelegation DELEGATION;

    static {
        try {
            DELEGATION = (IDelegation) ClassLoader.getSystemClassLoader().loadClass("DELEGATION_NAME").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
        DELEGATION.before(obj, method, clazz, _args, context);
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
            DELEGATION.afterThrowing(obj, method, clazz, args, ret, throwable, context);
            throw throwable;
        }
        if(!context.isContinue()) {
            ret = context._ret();
        }
        ret = DELEGATION.afterReturning(obj, method, clazz, args, ret, throwable, context);
    }



}
