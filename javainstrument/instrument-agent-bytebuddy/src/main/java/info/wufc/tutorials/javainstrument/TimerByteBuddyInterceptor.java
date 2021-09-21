package info.wufc.tutorials.javainstrument;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TimerByteBuddyInterceptor {

    @RuntimeType
    public void intercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> zuper,
                                 @Origin Method method) {
        long start = System.currentTimeMillis();
        try {
            zuper.call();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("consumes:" + (System.currentTimeMillis() - start));
        }
    }
}
