package info.wufc.tutorials.javabasic.primitive;

import org.junit.Test;

import java.nio.charset.Charset;

public class PrimitiveTypeTest {


    @Test
    public void testChar() {
        char x = '我';
        String a = "我";
        String b = "me";
        System.out.println(a.getBytes().length);
        System.out.println(a.getBytes(Charset.forName("UTF-8")).length);// 3
        System.out.println(a.getBytes(Charset.forName("GBK")).length); // 2
        System.out.println(b.getBytes().length);
        System.out.println(b.getBytes(Charset.forName("UTF-8")).length);
        System.out.println(b.getBytes(Charset.forName("GBK")).length);
    }

}
