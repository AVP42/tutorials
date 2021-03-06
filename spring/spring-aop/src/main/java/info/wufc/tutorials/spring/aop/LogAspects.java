package info.wufc.tutorials.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class LogAspects {

    //抽取公共的切入点表达式
    //1、本类引用
    //2、其他的切面引用
    @Pointcut("execution(public int info.wufc.tutorials.spring.aop.MathCalculator.*(..))")
    private void pointCut(){};

    //@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
    //JoinPoint一定要出现在参数列表的第一位
    @Before(value = "pointCut()")
    public void logStart(JoinPoint joinpoint) {
        System.out.println("logStart>>>>"+joinpoint.getSignature().getName()+">>>>"+ Arrays.toString(joinpoint.getArgs()));
//        System.out.println(1/ 0);
    }

    @After(value ="pointCut()")
    public void logEnd(JoinPoint joinpoint) {
        System.out.println("logEnd>>>>>"+joinpoint.getSignature().getName()+">>>>"+Arrays.toString(joinpoint.getArgs()));
//        System.out.println(1/ 0);
    }

    @AfterReturning(value ="pointCut()",returning="object")
    public void logReturn(Object object) {
        System.out.println("logReturn>>>>"+object);
        System.out.println(1/ 0);
    }

    @AfterThrowing(value = "pointCut()",throwing = "object")
    public void logException(Exception object) {
        System.out.println("logException>>>>"+object);
//        System.out.println(1/ 0);
    }

}
