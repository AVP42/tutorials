package info.wufc.javassist.classloader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @description:
 * @author: wufc@viomi.com.cn
 * @create: 2021-07-27 11:29
 * @since: v 3.1.0
 */
public class WritingClassLoader {


    public static class SampleLoader extends ClassLoader{
        private ClassPool pool;

        public SampleLoader() throws NotFoundException {
            pool = new ClassPool();
            pool.insertClassPath("./class");
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
//            try {
//                CtClass cc = pool.get(name);
//                // modify the ctClass object name
//
//            }
            return null;
        }


    }

}
