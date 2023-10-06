package project.house.builders.util;

import project.house.builders.domain.House;

public class HouseCreator {
    public static House createValidHouse(){
        return House.builder()
                .projectName("TestProject")
                .id(1L)
                .build();
    }

    public static House createHouseToBeSaved(){
        return House.builder()
                .projectName("TestProject")
                .build();
    }

    public static House createValidUpdatedHouse(){
        return House.builder()
                .projectName("TestProject Updated")
                .id(1L)
                .build();
    }
}
