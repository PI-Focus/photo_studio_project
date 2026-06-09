package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.IAboutDataBlock;

public class AboutDataBlockMock implements IAboutDataBlock {
    private final String logo;
    private final String description;
    private final String aboutImage;

    public AboutDataBlockMock(String logo, String description, String aboutImage) {
        this.logo = logo;
        this.description = description;
        this.aboutImage = aboutImage;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAboutImage() {
        return aboutImage;
    }
        
}
