package project.house.builders.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HousePostRequestBody {
    public String projectName;
    public Long engineerId;
    public Long architectId;
}
