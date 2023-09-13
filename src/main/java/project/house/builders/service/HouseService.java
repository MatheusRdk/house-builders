package project.house.builders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.exception.BadRequestException;
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

    @Transactional
    public House save(HousePostRequestBody housePostRequestBody){
        House house = House.builder()
                .projectName(housePostRequestBody.getProjectName())
                .build();
        houseRepository.save(house);

        Long engineerId = housePostRequestBody.getEngineerId();
        if(engineerId != null){
            Optional<Engineer> engineerOptional = engineerRepository.findById(engineerId);
            engineerOptional.ifPresentOrElse((engineer) -> {
                house.setEngineer(engineer);
                engineer.getHouses().add(house);
                engineerRepository.save(engineer);
            }, () -> {throw new BadRequestException("Engineer not found");});
        }

        Long architectId = housePostRequestBody.getArchitectId();
        if(architectId != null){
            Optional<Architect> architectOptional = architectRepository.findById(architectId);
            architectOptional.ifPresentOrElse((architect) -> {
                house.setArchitect(architect);
                architect.getHouses().add(house);
                architectRepository.save(architect);
            }, () -> {throw new BadRequestException("Architect not found");});
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
        House house = House.builder()
                .projectName(housePutRequestBody.getProjectName())
                .build();

        Long engineerId = housePutRequestBody.getEngineerId();
        if(engineerId != null){
            Optional<Engineer> engineerOptional = engineerRepository.findById(engineerId);
            engineerOptional.ifPresentOrElse((engineer) -> {
                house.setEngineer(engineer);
                engineer.getHouses().add(house);
            }, () -> {throw new BadRequestException("Engineer not found");});
        }

        Long architectId = housePutRequestBody.getArchitectId();
        if(architectId != null){
            Optional<Architect> architectOptional = architectRepository.findById(architectId);
            architectOptional.ifPresentOrElse((architect) -> {
                house.setArchitect(architect);
                architect.getHouses().add(house);
            }, () -> {throw new BadRequestException("Architect not found");});
        }

        house.setId(savedHouse.getId());
        houseRepository.save(house);
    }
}