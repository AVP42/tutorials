package info.wufc.javassist.classloader;


public class Window {
    private Rectangle rectangle;

    public Window() {
    }

    public Point getBaseX() {
        return this.rectangle.getSize();
    }

    @Override
    public String toString() {
        System.out.println(Rectangle.class.getClassLoader());
        System.out.println(Point.class.getClassLoader());
        return "Window{" +
                "rectangle=" + rectangle +
                '}';
    }
}
