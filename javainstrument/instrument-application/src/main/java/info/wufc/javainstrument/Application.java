package info.wufc.javainstrument;

import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        new TimerDemo().doSomething();

        TimeUnit.SECONDS.sleep(20L);

        new TimerDemo().doSomething();

        TimeUnit.SECONDS.sleep(20L);

        new TimerDemo().doSomething();

        TimeUnit.SECONDS.sleep(2L);
    }
}
