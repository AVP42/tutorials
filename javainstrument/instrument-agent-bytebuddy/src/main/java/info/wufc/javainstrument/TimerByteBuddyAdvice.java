package info.wufc.javainstrument;

import net.bytebuddy.asm.Advice;

/**
 * 当需要动态修改时, 通常用使用advice来实现
 */
public class TimerByteBuddyAdvice {

    @Advice.OnMethodEnter
    public static long onEnter(@Advice.AllArguments Object[] args) {
        return  System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static  void onExit(@Advice.AllArguments Object[] args, @Advice.Enter long start) {
        System.out.println(System.currentTimeMillis() - start);
    }

}
