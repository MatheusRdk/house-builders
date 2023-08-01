package project.house.builders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.exception.BadRequestException;
import project.house.builders.mapper.HouseMapper;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.requests.HousePutRequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final EngineerRepository engineerRepository;
    private final ArchitectRepository architectRepository;
//    private final EngineerService engineerService;
//    private final ArchitectService architectService;

    public List<House> listAll(){
        return houseRepository.findAll();
    }

    public List<House> findByName(String name){
        return houseRepository.findByProjectName(name);
    }

    public House findByIdOrThrowBadRequestException(long id){
        return houseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("House project not found."));
    }

    public House save(HousePostRequestBody housePostRequestBody){
        House house = House.builder()
                .projectName(housePostRequestBody.getProjectName())
                .build();
        houseRepository.save(house);

        Long engineerId = housePostRequestBody.getEngineerId();
        if(engineerId != null){
            Optional<Engineer> engineerOptional = engineerRepository.findById(engineerId);
            engineerOptional.ifPresent(engineer -> {
                house.setEngineer(engineer);
                engineer.getHouses().add(house);
                engineerRepository.save(engineer);
            });
        }

        Long architectId = housePostRequestBody.getArchitectId();
        if(architectId != null){
            Optional<Architect> architectOptional = architectRepository.findById(architectId);
            architectOptional.ifPresent(architect -> {
                house.setArchitect(architect);
                architect.getHouses().add(house);
                architectRepository.save(architect);
            });
        }
        return house;
    }

    public void delete(long id){
        House house = findByIdOrThrowBadRequestException(id);
        if(house.getEngineer() != null){
            house.getEngineer().getHouses().remove(house);
        }
        if(house.getArchitect() != null){
            house.getArchitect().getHouses().remove(house);
        }
        houseRepository.delete(house);
    }

    public void replace(HousePutRequestBody housePutRequestBody){
        House savedHouse = findByIdOrThrowBadRequestException(housePutRequestBody.getId());
        House house = HouseMapper.INSTANCE.toHouse(housePutRequestBody);
//        House.builder()
//                .projectName(housePutRequestBody.getProjectName())
//                .engineer(engineerService.findByIdOrThrowBadRequestException(housePutRequestBody.getEngineerId()))
//                .architect(architectService.findByIdOrThrowBadRequestException(housePutRequestBody.getArchitectId()))
//                .build();

        house.setId(savedHouse.getId());
        houseRepository.save(house);
    }
}