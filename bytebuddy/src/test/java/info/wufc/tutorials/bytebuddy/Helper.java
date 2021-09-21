package info.wufc.tutorials.bytebuddy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Helper {
    public static void outputClazz(byte[] bytes, String clazzName){
        FileOutputStream out = null;
        try {
            String path = ByteBuddyBaeldungGuide.class.getResource("/").getPath() + clazzName;
            File file = new File(path);
            out = new FileOutputStream(file);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
