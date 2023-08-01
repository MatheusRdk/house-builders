package project.house.builders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.house.builders.domain.Engineer;
import project.house.builders.requests.EngineerPostRequestBody;
import project.house.builders.requests.EngineerPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class EngineerMapper {
    public static final EngineerMapper INSTANCE = Mappers.getMapper(EngineerMapper.class);
    public abstract Engineer toEngineer(EngineerPostRequestBody engineerPostRequestBody);
    public abstract Engineer toEngineer(EngineerPutRequestBody engineerPutRequestBody);
}
