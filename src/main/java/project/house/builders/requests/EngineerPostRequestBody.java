package project.house.builders.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngineerPostRequestBody {
    @Schema(description = "This is the name of the engineer you are creating", example = "Jack Marston the engineer")
    public String name;
}
