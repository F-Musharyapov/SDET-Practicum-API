package pojo;

import lombok.Builder;
import lombok.Data;


import java.util.List;

import static helpers.TestDataHelper.*;

@Data
@Builder
public class User {

    private final Addition addition;

    public final String title = TITLE;
    public final Boolean verified = VERIFIED;
    public final List<Integer> important_numbers = IMPORTANT_NUMBERS;

    @Data
    @Builder
    public static class Addition {
        private final String additional_info;
        private final Integer additional_number;
    }
}
