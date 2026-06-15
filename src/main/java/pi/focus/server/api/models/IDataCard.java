package pi.focus.server.api.models;

/** 
 * Карточка данных с поддержкой навигации
 * Представляет собой элемент, при клике на который происходит переход по ссылке
 */
public interface IDataCard extends IImagedTextCard {
    /** @return целевой URL-адрес для перехода */
    String getLinkUrl();
}
