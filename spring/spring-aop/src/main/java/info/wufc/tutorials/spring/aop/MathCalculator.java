package info.wufc.tutorials.spring.aop;



public class MathCalculator {

    public int div(int i, int j) {
        System.out.println("MathCalculator >> div");
        return i / j;
    }


    public static void main(String[] args) throws Exception {
        Exception exception = null;
        try {
            System.out.println(1/ 0);
        }catch (Exception e){
            exception = e;
            try{
                throw new RuntimeException("sdsadf");
            }catch (Exception e1){
                e.addSuppressed(e1);
            }
            throw exception;
        }finally {
            try {
                throw new IllegalArgumentException("blablas");
            } catch (Exception e) {
                exception.addSuppressed(e);
            }
        }
    }
}

