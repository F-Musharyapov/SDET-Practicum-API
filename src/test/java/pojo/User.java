package pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import static helpers.TestDataHelper.*;

/**
 * Класс пользователя с его переменными
 */
@Data
@Builder
public class User {

    /**
     * Экземпляр класса Addition
     */
    private final Addition addition;

    private final String title = TITLE;
    private final Boolean verified = VERIFIED;
    private final List<Integer> important_numbers = IMPORTANT_NUMBERS;

    /**
     * Класс Addition с дополнительными полями
     */
    @Data
    @Builder
    public static class Addition {
        private final String additional_info;
        private final Integer additional_number;
    }
}
