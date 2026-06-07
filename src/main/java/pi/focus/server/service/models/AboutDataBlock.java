package pi.focus.server.service.models;

import pi.focus.server.api.models.IAboutDataBlock;

public record AboutDataBlock (String logo, String description) implements IAboutDataBlock {
    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
