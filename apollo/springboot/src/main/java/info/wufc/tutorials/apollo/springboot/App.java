package info.wufc.tutorials.apollo.springboot;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(App.AppProperties.class)
public class App {

    @Value("${my_key:ssss}")
    private String value;

    private AppProperties properties;

    public App(AppProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("app.id","apollo-from-env");
//        System.setProperty("app.id", System.getenv("HOSTNAME"));
//        Config config = ConfigService.getConfig("viomi-infra.component-biz-log");
//        String property = config.getProperty("viomi.biz.log.spring-config-enabled", "default");
//        System.out.println(property);
        SpringApplication.run(App.class, args);
    }

    @Scheduled(fixedDelay = 1000L)
    public String getValue(){
//        System.out.println(value);
        for(Map.Entry entry: properties.getMap().entrySet()){
            System.out.print(entry.getKey());
            System.out.println(entry.getValue());
        }

        return value;
    }

    @ConfigurationProperties(prefix = "info.wufc.tutorials.apollo")
    public static class AppProperties{
        private Map<String, Integer> map;
        private List<String> list;

        public Map<String, Integer> getMap() {
            return map;
        }

        public void setMap(Map<String, Integer> map) {
            this.map = map;
        }
    }




}
