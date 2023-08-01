package project.house.builders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.house.builders.domain.House;
import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.requests.HousePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class HouseMapper {
    public static final HouseMapper INSTANCE = Mappers.getMapper(HouseMapper.class);
    public abstract House toHouse(HousePostRequestBody housePostRequestBody);
    public abstract House toHouse(HousePutRequestBody housePutRequestBody);
}
