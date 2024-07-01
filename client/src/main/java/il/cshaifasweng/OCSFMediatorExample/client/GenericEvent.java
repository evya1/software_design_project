package il.cshaifasweng.OCSFMediatorExample.client;

public class GenericEvent<T> {
    private T data;

    public GenericEvent(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}