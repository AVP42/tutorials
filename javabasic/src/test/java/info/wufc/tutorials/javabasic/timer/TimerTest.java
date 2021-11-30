package info.wufc.tutorials.javabasic.timer;

import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {


    @Test
    public void exitWhenOneTaskThrowException(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTest.run 1");
            }
        }, 2000, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTest.run 1");
            }
        }, 2000, 1000);
    }

}
