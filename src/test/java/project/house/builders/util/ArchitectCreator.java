package project.house.builders.util;

import project.house.builders.domain.Architect;

public class ArchitectCreator {
    public static Architect createValidArchitect(){
        return Architect.builder()
                .name("ArchitectTest")
                .id(1L)
                .build();
    }

    public static Architect createArchitectToBeSaved(){
        return Architect.builder()
                .name("ArchitectTest")
                .build();
    }

    public static Architect createValidUpdatedArchitect(){
        return Architect.builder()
                .name("ArchitectTest Updated")
                .id(1L)
                .build();
    }
}
