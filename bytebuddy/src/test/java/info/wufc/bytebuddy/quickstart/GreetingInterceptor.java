package info.wufc.bytebuddy.quickstart;

public class GreetingInterceptor {
    public Object greet(Object argument) {
        return "Hello from " + argument;
    }
}
