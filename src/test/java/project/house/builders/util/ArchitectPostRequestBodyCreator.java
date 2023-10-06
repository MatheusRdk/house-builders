package project.house.builders.util;

import project.house.builders.requests.ArchitectPostRequestBody;

public class ArchitectPostRequestBodyCreator {
    public static ArchitectPostRequestBody createArchitectPostRequestBody() {
        return ArchitectPostRequestBody.builder()
                .name(ArchitectCreator.createArchitectToBeSaved().getName())
                .build();
    }
}
