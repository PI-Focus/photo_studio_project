package pi.focus.server.api.models;

public interface ITab<T> {
    String getTabName();
    T getData();
}
