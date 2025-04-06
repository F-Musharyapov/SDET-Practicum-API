package helpers;

import java.util.List;

/**
 * Класс с константами
 */
public class TestDataHelper {

    /**
     * Идентификатор additionalInfo
     */
    public static final String ADD_INFO = "ДопИнфа";

    /**
     * Идентификатор additionalInfo Update
     */
    public static final String ADD_INFO_UPDATE = "ДопИнфа Обновленный";

    /**
     * Идентификатор additionalNumber
     */
    public static final int ADD_NUMBER = 777;

    /**
     * Идентификатор additionalNumber Update
     */
    public static final int ADD_NUMBER_UPDATE = 67888;

    /**
     * Идентификатор importantNumbers
     */
    public static final List<Integer> IMPORTANT_NUMBERS = List.of(11, 22, 33);

    /**
     * Идентификатор importantNumbers Update
     */
    public static final List<Integer> IMPORTANT_NUMBERS_UPDATE = List.of(34, 56, 78);

    /**
     * Идентификатор title
     */
    public static final String TITLE = "Заголовок 777";

    /**
     * Идентификатор title Update
     */
    public static final String TITLE_UPDATE = "Заголовок 777 Обновленный";

    /**
     * Идентификатор verified
     */
    public static final boolean VERIFIED = true;

    /**
     * Статус код успешного добавления
     */
    public static final int STATUS_CODE_OK = 200;

    /**
     * Статус код успешного удаления
     */
    public static final int STATUS_CODE_NO_CONTENT = 204;

    /**
     * Статус код отсутствия объекта get
     */
    public static final int STATUS_INTERAL_SERVER_ERROR = 500;

}