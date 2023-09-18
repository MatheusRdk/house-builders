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
public class HousePostRequestBody {
    @Schema(description = "This is the project name (house name)", example = "Silva's family house")
    public String projectName;
    @Schema(description = "This is the id of the engineer that you want to add to this house (optional)", example = "3")
    public Long engineerId;
    @Schema(description = "This is the id of the architect that you want to add to this house (optional)", example = "2")
    public Long architectId;
}
