package info.wufc.tutorials.bytebuddy.usercase.monitor;

import java.util.Random;

public class BizMethod {
    public String queryUserInfo(String uid, String token) throws InterruptedException {
        Thread.sleep(new Random().nextInt(500));
        return uid + "_" + token;
    }
}
