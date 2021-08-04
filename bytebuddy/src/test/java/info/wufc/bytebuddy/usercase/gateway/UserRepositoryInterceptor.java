package info.wufc.bytebuddy.usercase.gateway;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class UserRepositoryInterceptor {

    public static String intercept(@Origin Method method, @AllArguments Object[] args) {
        return "intercepted, do your own thing now";
    }
}
