package project.house.builders.util;

import project.house.builders.requests.HousePutRequestBody;

public class HousePutRequestBodyCreator {
    public static HousePutRequestBody createHousePutRequestBody(){
        return HousePutRequestBody.builder()
                .id(HouseCreator.createValidUpdatedHouse().getId())
                .projectName(HouseCreator.createHouseToBeSaved().getProjectName())
                .engineerId(1L)
                .architectId(1L)
                .build();
    }

    public static HousePutRequestBody createHousePutRequestBodyOnlyWithName(){
        return HousePutRequestBody.builder()
                .id(HouseCreator.createValidUpdatedHouse().getId())
                .projectName(HouseCreator.createValidUpdatedHouse().getProjectName())
                .build();
    }
}
