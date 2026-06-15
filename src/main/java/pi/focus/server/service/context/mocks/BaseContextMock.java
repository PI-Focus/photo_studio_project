package pi.focus.server.service.context.mocks;

import pi.focus.server.api.context.IBaseContext;

/**
 * Мок-реализация базового контекста приложения.
 * Содержит демонстрационные контактные данные студии, ИНН и пути к логотипам.
 */
public class BaseContextMock implements IBaseContext{

    @Override
    public String getLogoBright() {
        return MocksDefines.LOGO_BRIGHT_PATH;
    }

    @Override
    public String getLogoDark() {
        return MocksDefines.LOGO_DARK_PATH;
    }

    @Override
    public String getProfileBright() {
        return MocksDefines.PROFILE_BRIGHT_PATH;
    }

    @Override
    public String getProfileDark() {
        return MocksDefines.PROFILE_DARK_PATH;
    }

    @Override
    public String getInn() {
        return "666666666666";
    }

    @Override
    public String getOgrnip() {
        return "026766666666660";
    }

    @Override
    public String getCompanyName() {
        return "ИП Окуловский Владимир Андреевич";
    }

    @Override
    public String getEmail() {
        return "pi_focus@yandex.ru";
    }

    @Override
    public String getPhone() {
        return "8-666-666-66-66";
    }

    @Override
    public String getAddress() {
        return "г. Ярославль, ул. Союзная, 144";
    }
}
