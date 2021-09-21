package info.wufc.tutorials.javabasic.overide;

import java.io.IOException;

public class Foo {

    protected Jeo getSomething(int key) throws IOException, ClassNotFoundException {
        return new Jeo();
    }

}
