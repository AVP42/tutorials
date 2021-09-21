package info.wufc.tutorials.javainstrument;

import java.util.Random;

public class TimerDemo {
    public void doSomething()  {
        System.out.println("TimerDemo.doSomething");
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
