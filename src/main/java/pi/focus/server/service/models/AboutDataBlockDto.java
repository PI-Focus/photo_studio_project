package pi.focus.server.service.models;

import pi.focus.server.api.models.IAboutDataBlock;

/**
 * DTO для блока информации «О компании».
 * 
 * @param description текстовое описание студии
 * @param aboutImage путь к презентационному изображению
 */
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
