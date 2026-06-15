package pi.focus.server.service.context.mocks;

/**
 * Утилитарный класс, содержащий статические пути к ресурсам для мок-данных.
 * Централизованно хранит ссылки на изображения-заглушки и логотипы.
 */
public final class MocksDefines {
     /** Путь к тестовому изображению-заглушке */
    public static final String TEST_IMAGE_PATH = "/images/placeholder.png";
    public static final String LOGO_BRIGHT_PATH = "/images/logo-bright.svg";
    public static final String LOGO_DARK_PATH = "/images/logo-dark.svg";
    public static final String PROFILE_BRIGHT_PATH = "/images/profile-bright.svg";
    public static final String PROFILE_DARK_PATH = "/images/profile-dark.svg";

    /**
     * Закрытый конструктор.
     * @throws UnsupportedOperationException если была предпринята попытка создания экземпляра
     */
    private MocksDefines() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
