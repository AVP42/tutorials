package info.wufc.tutorials.spring.aop;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class Config {

    //业务逻辑类加入到容器中
    @Bean
    public MathCalculator mathCalculator() {
        System.out.println("mathCalculator bean");
        return new MathCalculator();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }

}
