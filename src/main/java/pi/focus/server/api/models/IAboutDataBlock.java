package pi.focus.server.api.models;

/** 
 * Интерфейс блока «О компании».
 * Содержит описание и главное изображение для презентации студии.
 */
public interface IAboutDataBlock {
    /** @return текстовое описание преимуществ студии */
    String getDescription();
    /** @return путь к изображению блока */
    String getAboutImage();
} 
