package pi.focus.server.service.context;

import pi.focus.server.api.context.IBaseContext;

/**
 * DTO реализации базового контекста приложения.
 * Хранит глобальные настройки сайта: логотипы, юридическую информацию и контакты.
 * 
 * @param logoBright путь к светлому логотипу
 * @param logoDark путь к темному логотипу
 * @param profileBright путь к светлой иконке профиля
 * @param profileDark путь к темной иконке профиля
 * @param inn ИНН организации
 * @param ogrnip ОГРНИП организации
 * @param companyName название компании
 * @param email контактный email
 * @param phone контактный телефон
 * @param address физический адрес студии
 */
public record BaseContextDto(
    String logoBright,
    String logoDark,
    String profileBright,
    String profileDark,
    String inn,
    String ogrnip,
    String companyName,
    String email,
    String phone,
    String address
) implements IBaseContext {

    @Override
    public String getLogoBright() {
        return logoBright;
    }

    @Override
    public String getLogoDark() {
        return logoDark;
    }

    @Override
    public String getProfileBright() {
        return profileBright;
    }

    @Override
    public String getProfileDark() {
        return profileDark;
    }

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

