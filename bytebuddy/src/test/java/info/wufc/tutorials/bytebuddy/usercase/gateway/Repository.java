package info.wufc.tutorials.bytebuddy.usercase.gateway;

public abstract class Repository<T> {
    public abstract T queryData(int id);
}
