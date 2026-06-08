package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IAboutDataBlock;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
public class AboutDataBlockMock implements IAboutDataBlock {

    @Override
    public String getLogo() {
        return MocksDefines.TEST_IMAGE_PATH;
    }

    @Override
    public String getDescription() {
        return "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии..." +
            "описание фотостудии блаблабла описание.... фотостудии...";
    }

    @Override
    public String getAboutImage() {
        return MocksDefines.TEST_IMAGE_PATH;
    }
    
}
