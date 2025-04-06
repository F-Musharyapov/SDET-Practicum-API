package pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import static helpers.TestDataHelper.*;

@Data
@Builder
public class UserUpdate {

    private final UserUpdate.Addition addition;

    public final String title = TITLE_UPDATE;
    public final Boolean verified = VERIFIED;
    public final List<Integer> important_numbers = IMPORTANT_NUMBERS_UPDATE;

    @Data
    @Builder
    public static class Addition {
        private final String additional_info;
        private final Integer additional_number;
    }
}
