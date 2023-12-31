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
public class ArchitectPostRequestBody {
    @Schema(description = "This is the name of the architect you are creating", example = "Orion Jackson")
    public String name;
}
