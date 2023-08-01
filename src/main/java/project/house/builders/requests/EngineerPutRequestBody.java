package project.house.builders.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineerPutRequestBody {
    private Long id;
    private String name;
}
