package info.wufc.tutorials.javainstrument;

import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("==============================================instanceMethod");
                System.out.println("demo final return:" + new TimerDemo(3).doSomething(2));
                TimeUnit.SECONDS.sleep(2L);
                System.out.println("==============================================staticMethod");
                System.out.println("demoStatic final return:" + TimerDemo.doSomethingStatic(22));
            } catch (Exception e) {
            }
            TimeUnit.SECONDS.sleep(30L);
        }

    }
}
