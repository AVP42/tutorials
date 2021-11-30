package info.wufc.tutorials.javabasic.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class TestCountDownLatch {

    /**
     * 利用CountDownLatch 实现等待其他线程执行完再执行
     * @throws InterruptedException
     */
    @Test
    public void mainThreadBlockUtilWorkerCompletion() throws InterruptedException {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(5);
        List<Thread> threads =
            Stream.generate(() -> new Thread(new Worker(outputScraper, latch))).limit(5).collect(Collectors.toList());
        threads.forEach(Thread::start);
        // 等待latch 倒数到0的时候，才会继续下面的任务
        latch.await();
        outputScraper.add("Latch released");

        assertThat(outputScraper).containsExactly("Counted Down", "Counted Down", "Counted Down", "Counted Down",
            "Counted Down", "Latch released");

    }



    class Worker implements Runnable {
        List<String> outputScraper;
        CountDownLatch latch;

        public Worker(List<String> outputScraper, CountDownLatch latch) {
            this.outputScraper = outputScraper;
            this.latch = latch;
        }

        @Override
        public void run() {
            doSomeWork();
            outputScraper.add("Counted Down");
            latch.countDown();
        }

        private void doSomeWork() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("Worker.doSomeWork");
        }
    }

}
