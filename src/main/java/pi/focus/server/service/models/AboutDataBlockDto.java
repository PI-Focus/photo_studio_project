package pi.focus.server.service.models;

import pi.focus.server.api.models.IAboutDataBlock;

public record AboutDataBlockDto(
    String description, 
    String aboutImage
) implements IAboutDataBlock {

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAboutImage() {
        return aboutImage;
    }
}
