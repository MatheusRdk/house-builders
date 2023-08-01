package project.house.builders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.exception.BadRequestException;
import project.house.builders.mapper.ArchitectMapper;
import project.house.builders.mapper.EngineerMapper;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.requests.ArchitectPostRequestBody;
import project.house.builders.requests.ArchitectPutRequestBody;
import project.house.builders.requests.EngineerPutRequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchitectService {
    private final ArchitectRepository architectRepository;

    public List<Architect> listAll(){
        return architectRepository.findAll();
    }

    public List<Architect> findByName(String name){
        return architectRepository.findByName(name);
    }

    public Architect findByIdOrThrowBadRequestException(long id){
        return architectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Architect not found."));
    }

    public Architect save(final ArchitectPostRequestBody architectPostRequestBody){
        final var architect = Architect.builder()
                .name(architectPostRequestBody.getName())
                .build();
        return architectRepository.save(architect);
    }

    public void delete(long id){
        architectRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(ArchitectPutRequestBody architectPutRequestBody) {
        Architect savedArchitect = findByIdOrThrowBadRequestException(architectPutRequestBody.getId());
        Architect architect = ArchitectMapper.INSTANCE.toArchitect(architectPutRequestBody);
        architect.setId(savedArchitect.getId());
        architectRepository.save(architect);
    }
}
