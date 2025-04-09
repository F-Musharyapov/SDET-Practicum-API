package pojo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Класс пользователя с его переменными
 */
@Data
@Builder
@EqualsAndHashCode(exclude = {"id"})
public class User {

    /**
     * Экземпляр класса Addition
     */
    private final Addition addition;

    private final String title;
    private final Boolean verified;
    private final List<Integer> important_numbers;

}
