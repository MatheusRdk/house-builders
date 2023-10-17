package project.house.builders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.requests.HousePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class HouseMapper {
    public static final HouseMapper INSTANCE = Mappers.getMapper(HouseMapper.class);
    public abstract House toHouse(HousePostRequestBody housePostRequestBody);
    public abstract House toHouse(HousePutRequestBody housePutRequestBody);
    public House toHouseWithEngAndArch(HousePutRequestBody housePutRequestBody) {
        if ( housePutRequestBody == null ) {
            return null;
        }

        House.HouseBuilder house = House.builder();

        if (housePutRequestBody.getEngineerId() != null){
            Engineer engineer = Engineer.builder().id(housePutRequestBody.getEngineerId()).build();
            house.engineer(engineer);
        }
        if (housePutRequestBody.getArchitectId() != null){
            Architect architect = Architect.builder().id(housePutRequestBody.getArchitectId()).build();
            house.architect(architect);

        }

        house.id( housePutRequestBody.getId() );
        house.projectName( housePutRequestBody.getProjectName() );

        return house.build();
    }
}
