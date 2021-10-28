package info.wufc.tutorials.javainstrument.delegation;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class AdviceConstructDelegation {


    public Object afterConstruct(Constructor method, Class clazz, Object[] args, Object field) {
        System.out.println(">>>>>>>>onConstruct>>>>>>>>");
        System.out.println("method: " +method);
        System.out.println("clazz:" +clazz);
        System.out.println("args:" + Arrays.toString(args));
        System.out.println("field:" + field);
        field = 999;
        return field;
    }

}
