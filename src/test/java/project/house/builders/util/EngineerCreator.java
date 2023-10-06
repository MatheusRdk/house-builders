package project.house.builders.util;

import project.house.builders.domain.Engineer;

public class EngineerCreator {
    public static Engineer createValidEngineer(){
        return Engineer.builder()
                .name("EngineerTest")
                .id(1L)
                .build();
    }

    public static Engineer createEngineerToBeSaved(){
        return Engineer.builder()
                .name("EngineerTest")
                .build();
    }

    public static Engineer createValidUpdatedEngineer(){
        return Engineer.builder()
                .name("EngineerTest Updated")
                .id(1L)
                .build();
    }
}
