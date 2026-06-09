package pi.focus.server.service.context.mocks;

import pi.focus.server.api.context.IBaseContext;

public class BaseContextMock implements IBaseContext{

    @Override
    public String getINN() {
        return "666666666666";
    }

    @Override
    public String getOGRNIP() {
        return "026766666666660";
    }

    @Override
    public String getCompanyName() {
        return "ИП Окуловский Владимир Андреевич";
    }

    @Override
    public String getMail() {
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
