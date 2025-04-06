package pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import static helpers.TestDataHelper.*;

/**
 * Обновленный класс пользователя с его переменными
 */
@Data
@Builder
public class UserUpdate {

    /**
     * Экземпляр класса UpdateAddition
     */
    private final UpdateAddition addition;

    private final String title = TITLE_UPDATE;
    private final Boolean verified = VERIFIED;
    private final List<Integer> important_numbers = IMPORTANT_NUMBERS_UPDATE;

    /**
     * Класс UpdateAddition с дополнительными полями
     */
    @Data
    @Builder
    public static class UpdateAddition {
        private final String additional_info;
        private final Integer additional_number;
    }
}
