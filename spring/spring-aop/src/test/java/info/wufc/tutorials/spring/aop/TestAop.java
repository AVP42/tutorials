package info.wufc.tutorials.spring.aop;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        mathCalculator.div(10, 1);
        applicationContext.close();
    }

}
