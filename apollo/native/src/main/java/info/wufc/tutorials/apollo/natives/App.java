package info.wufc.tutorials.apollo.natives;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

public class App {

    public static void main(String[] args) {
        System.setProperty("app.id","apollo-from-env2");
        Config appConfig = ConfigService.getConfig("viomi-infra.vts-agent");
        System.out.println(appConfig.getPropertyNames());
    }
}
