package project.house.builders.util;

import project.house.builders.requests.EngineerPutRequestBody;

public class EngineerPutRequestBodyCreator {
    public static EngineerPutRequestBody createEngineerPutRequestBody(){
        return EngineerPutRequestBody.builder()
                .id(EngineerCreator.createValidUpdatedEngineer().getId())
                .name(EngineerCreator.createValidUpdatedEngineer().getName())
                .build();
    }
}
