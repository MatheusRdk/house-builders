package project.house.builders.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineerPutRequestBody {
    @Schema(description = "This is the id of the engineer you want to change", example = "1")
    private Long id;
    @Schema(description = "This is the new name of the engineer you are changing", example = "John Marston")
    private String name;
}
