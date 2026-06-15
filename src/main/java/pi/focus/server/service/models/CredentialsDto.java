package pi.focus.server.service.models;

import pi.focus.server.api.models.ICredentials;

public class CredentialsDto implements ICredentials {
    private String login;
    private String password;
    private String phoneNumber;
    private String email;
    private String error;

    public CredentialsDto(String login, String password, String phoneNumber, String email, String error) {
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.error = error;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }
}
