package info.wufc.tutorials.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.pool.TypePool;

class Bar extends Foo{

    @BindingPriority(3)
    public static String sayHelloBar() {
        return "Holla in Bar!";
    }

    /** BindingPriority 默认值是1*/
    @BindingPriority(2)
    public static String sayBar() {
        return "bar";
    }

    public String sayBarName() {
        return Bar.class.getSimpleName() + " - Bar";
    }

     public static void main(String[] args) throws NoSuchFieldException {
         TypePool typePool = TypePool.Default.ofSystemLoader();
         Class geo = new ByteBuddy()
                 .redefine(typePool.describe("info.wufc.other.Geo").resolve(),
                         ClassFileLocator.ForClassLoader.ofSystemLoader())
                 .defineField("qux", String.class)
                 .make()
                 .load(ClassLoader.getSystemClassLoader())
                 .getLoaded();
         System.out.println(geo.getDeclaredField("qux"));
     }
}
