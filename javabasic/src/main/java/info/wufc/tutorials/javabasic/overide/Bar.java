package info.wufc.tutorials.javabasic.overide;

import java.io.FileNotFoundException;

public class Bar extends Foo {

    /**
     * 重写 override  方法签名相同method signature  即方法名相同， 参数列表相同
     * 1.返回值可以是其派生类
     * 2.访问权限可以更广
     * 3.外抛异常可以减少，可以是其派生类，可以增加任何非检查异常
     *
     * 而重载 overload 只需要方法名相同， 参数列表不同即可
     */
    @Override
    public Pek getSomething(int key) throws RuntimeException,FileNotFoundException {
        return new Pek();
    }
}
