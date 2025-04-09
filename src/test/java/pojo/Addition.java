package pojo;

import lombok.Builder;
import lombok.Data;

/**
 * Класс Addition с дополнительными полями
 */
@Data
@Builder
public class Addition {
    private final String additional_info;
    private final Integer additional_number;
}
