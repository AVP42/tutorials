package info.wufc.bytebuddy.usercase.gateway;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class GatewayTest {


    @Test
    public void generateClassFile() throws IOException {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy().subclass(TypeDescription.Generic.Builder.parameterizedType(Repository.class, String.class).build())
                .name(Repository.class.getPackage().getName().concat(".").concat("UserRepository"))
                .method(ElementMatchers.named("queryData"))
                .intercept(MethodDelegation.to(UserRepositoryInterceptor.class))
                .annotateMethod(AnnotationDescription.Builder.ofType(RpcGatewayMethod.class).define("methodName", "queryData").define("methodDesc", "查询数据").build())
                .annotateType(AnnotationDescription.Builder.ofType(RpcGatewayClazz.class)
                        .define("alias", "dataApi").define("clazzDesc", "查询数据")
                        .define("timeout", 350L).build())
                .make();
        dynamicType.saveIn(new File(getClass().getResource("/").getPath()));
    }

    @Test
    public void outputAnnotation() throws Exception {
        Class<?> clazz = Class.forName("info.wufc.bytebuddy.usercase.gateway.UserRepository");
        RpcGatewayClazz clazzAnnotation = clazz.getAnnotation(RpcGatewayClazz.class);
        System.out.println(clazzAnnotation.alias());
        System.out.println(clazzAnnotation.clazzDesc());
        System.out.println(clazzAnnotation.timeout());

        RpcGatewayMethod methodAnnotation = clazz.getMethod("queryData", int.class).getAnnotation(RpcGatewayMethod.class);
        System.out.println(methodAnnotation.methodName());
        System.out.println(methodAnnotation.methodDesc());
    }

    @Test
    public void testRun() throws Exception {
        Class<?> clazz = Class.forName("info.wufc.bytebuddy.usercase.gateway.UserRepository");
        Repository<String> repository = (Repository<String>) clazz.newInstance();
        System.out.println(repository.queryData(10001));

    }
}
