package project.house.builders.util;

import project.house.builders.requests.HousePostRequestBody;

public class HousePostRequestBodyCreator {
    public static HousePostRequestBody createHousePostRequestBody(){
        return HousePostRequestBody.builder()
                .projectName(HouseCreator.createHouseToBeSaved().getProjectName())
                .engineerId(1L)
                .architectId(1L)
                .build();
    }

    public static HousePostRequestBody createHousePostRequestBodyOnlyWithName(){
        return HousePostRequestBody.builder()
                .projectName(HouseCreator.createHouseToBeSaved().getProjectName())
                .build();
    }
}
