package info.wufc.tutorials.javainstrument;

import java.util.Random;

public class TimerDemo {
    private volatile Object _$EnhancedClassField_vts;

    public TimerDemo(int j) {
        System.out.println("TimerDemo.TimerDemo");
    }

    public int doSomething(int i)  {
        System.out.println("TimerDemo.doSomething: " + _$EnhancedClassField_vts + " i：" + i);
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(1/0);
        return 1;
    }

    public static int doSomethingStatic(int i)  {
        System.out.println("TimerDemo.doSomething:" + i);
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(1/0);
        return 1;
    }
}
