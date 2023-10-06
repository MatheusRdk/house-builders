package project.house.builders.util;

import project.house.builders.requests.ArchitectPutRequestBody;

public class ArchitectPutRequestBodyCreator {
    public static ArchitectPutRequestBody createArchitectPutRequestBody() {
        return ArchitectPutRequestBody.builder()
                .id(ArchitectCreator.createValidUpdatedArchitect().getId())
                .name(ArchitectCreator.createValidUpdatedArchitect().getName())
                .build();
    }
}
