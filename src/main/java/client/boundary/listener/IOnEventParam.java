package client.boundary.listener;

public interface IOnEventParam<T> {
    void signal(T value);
}
