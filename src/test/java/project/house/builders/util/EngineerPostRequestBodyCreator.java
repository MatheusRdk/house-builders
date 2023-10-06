package project.house.builders.util;

import project.house.builders.requests.EngineerPostRequestBody;

public class EngineerPostRequestBodyCreator {
    public static EngineerPostRequestBody createEngineerPostRequestBody(){
        return EngineerPostRequestBody.builder()
                .name(EngineerCreator.createEngineerToBeSaved().getName())
                .build();
    }
}
