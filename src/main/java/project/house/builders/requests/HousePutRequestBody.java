package project.house.builders.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HousePutRequestBody {
    @Schema(description = "This is the id of the house you want to change", example = "1")
    private Long id;
    @Schema(description = "This is the project name (house name)", example = "Silva's family house")
    private String projectName;
    @Schema(description = "This is the id of the engineer that you want to add to this house (optional)", example = "3")
    private Long engineerId;
    @Schema(description = "This is the id of the architect that you want to add to this house (optional)", example = "2")
    private Long architectId;
}
