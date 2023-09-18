package project.house.builders.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArchitectPutRequestBody {
    @Schema(description = "This is the id of the architect you are changing", example = "15")
    private Long id;
    @Schema(description = "This is the new name of the architect you are changing", example = "Rodrigo Jackson")
    private String name;

}
