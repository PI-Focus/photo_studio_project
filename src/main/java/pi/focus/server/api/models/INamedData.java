package pi.focus.server.api.models;

public interface INamedData<T> {
    String getTabName();
    T getData();
}
