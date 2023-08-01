package project.house.builders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.house.builders.domain.Architect;
import project.house.builders.requests.ArchitectPostRequestBody;
import project.house.builders.requests.ArchitectPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class ArchitectMapper {
    public static final ArchitectMapper INSTANCE = Mappers.getMapper(ArchitectMapper.class);
    public abstract Architect toArchitect(ArchitectPostRequestBody architectPostRequestBody);
    public abstract Architect toArchitect(ArchitectPutRequestBody architectPutRequestBody);
}
