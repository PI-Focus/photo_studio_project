package pi.focus.server.service.context;

import pi.focus.server.api.context.IBaseContext;

public record BaseContextDto(
    String inn,
    String ogrnip,
    String companyName,
    String email,
    String phone,
    String address
) implements IBaseContext {

    @Override
    public String getInn() {
        return inn;
    }

    @Override
    public String getOgrnip() {
        return ogrnip;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAddress() {
        return address;
    }
}

