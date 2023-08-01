package project.house.builders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.house.builders.domain.Engineer;
import project.house.builders.exception.BadRequestException;
import project.house.builders.mapper.EngineerMapper;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.requests.EngineerPostRequestBody;
import project.house.builders.requests.EngineerPutRequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EngineerService {
    private final EngineerRepository engineerRepository;

    public List<Engineer> listAll() {
        return engineerRepository.findAll();
    }

    public List<Engineer> findByName(String name) {
        return engineerRepository.findByName(name);
    }

    public Engineer findByIdOrThrowBadRequestException(long id) {
        return engineerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Engineer not found."));
    }

    public Engineer save(final EngineerPostRequestBody engineerPostRequestBody) {
        final var engineer = Engineer.builder()
                .name(engineerPostRequestBody.getName())
                .build();
        return engineerRepository.save(engineer);
    }

    public void delete(long id) {
        engineerRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(EngineerPutRequestBody engineerPutRequestBody) {
        Engineer savedEngineer = findByIdOrThrowBadRequestException(engineerPutRequestBody.getId());
        Engineer engineer = EngineerMapper.INSTANCE.toEngineer(engineerPutRequestBody);
        engineer.setId(savedEngineer.getId());
        engineerRepository.save(engineer);
    }
}