package pi.focus.server.models.mocks;

import pi.focus.server.models.IAboutDataBlock;

public class AboutDataBlockMock implements IAboutDataBlock {

    @Override
    public String getLogo() {
        return "/images/placeholder.png";
    }

    @Override
    public String getDescription() {
        return "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии...";
    }
    
}
