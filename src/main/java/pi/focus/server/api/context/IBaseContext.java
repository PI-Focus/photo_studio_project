package pi.focus.server.api.context;

/**
 * Базовый контекст приложения.
 * Содержит глобальную информацию, отображаемую на всех страницах
 */
public interface IBaseContext {
    /** @return путь к светлому логотипу сайта */
    String getLogoBright();
    /** @return путь к темному логотипу сайта */
    String getLogoDark();
    /** @return путь к светлой иконке профиля */
    String getProfileBright();
    /** @return путь к темной иконке профиля */
    String getProfileDark();
    /** @return путь к ИНН организации */
    String getInn();
    /** @return путь к ОРГНИП организации */
    String getOgrnip();
    /** @return полное юридическое название организации */
    String getCompanyName();
    /** @return путь к контактной почте организации */
    String getEmail();
    /** @return путь к контактному номеру телефона организации */
    String getPhone();
    /** @return путь к фактическому адресу фотостудии */
    String getAddress();
}
