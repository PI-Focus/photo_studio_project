package pi.focus.server.api.models;

public interface ICredentials {
    String getLogin();
    String getPassword();
    String getPhoneNumber();
    String getEmail();
    String getError();
    void setError(String error);
}
