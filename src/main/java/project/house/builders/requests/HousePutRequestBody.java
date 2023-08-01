package project.house.builders.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HousePutRequestBody {
    private Long id;
    private String projectName;
    private Long engineerId;
    private Long architectId;
}
