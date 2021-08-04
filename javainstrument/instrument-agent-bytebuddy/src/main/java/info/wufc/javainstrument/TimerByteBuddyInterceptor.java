package info.wufc.javainstrument;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class TimerByteBuddyInterceptor {

    @RuntimeType
    public static void intercept(@SuperCall Callable<?> callable) {
        long start = System.currentTimeMillis();
        try {
            callable.call();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("consumes:" + (System.currentTimeMillis() - start));
        }
    }
}
