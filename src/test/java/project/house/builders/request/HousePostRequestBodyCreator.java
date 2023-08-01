package project.house.builders.request;

import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.util.HouseCreator;

public class HousePostRequestBodyCreator {
    public static HousePostRequestBody createHousePostRequestBody(){
        return HousePostRequestBody.builder()
                .projectName(HouseCreator.createHouseToBeSaved().getProjectName())
                .engineerId(1L)
                .architectId(1L)
                .build();
    }
}
