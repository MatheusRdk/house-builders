package project.house.builders.repository;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.house.builders.domain.Engineer;
import project.house.builders.util.EngineerCreator;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for engineer repository")
class EngineerRepositoryTest {
    @Autowired
    private EngineerRepository engineerRepository;

    @Test
    @DisplayName("Save persists engineer when succesful")
    void save_PersistsEngineer_WhenSuccessful(){
        Engineer engineerToBeSaved = EngineerCreator.createEngineerToBeSaved();
        Engineer savedEngineer = engineerRepository.save(engineerToBeSaved);

        Assertions.assertThat(savedEngineer).isNotNull();
        Assertions.assertThat(savedEngineer.getId()).isNotNull();
        Assertions.assertThat(savedEngineer.getName()).isEqualTo(engineerToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates engineer when succesful")
    void save_UpdatesEngineer_WhenSuccessful(){
        Engineer engineerToBeSaved = EngineerCreator.createEngineerToBeSaved();
        Engineer savedEngineer = engineerRepository.save(engineerToBeSaved);
        savedEngineer.setName("New name");
        Engineer engineerUpdated = engineerRepository.save(savedEngineer);

        Assertions.assertThat(engineerUpdated).isNotNull();
        Assertions.assertThat(engineerUpdated.getId()).isNotNull();
        Assertions.assertThat(engineerUpdated.getName()).isEqualTo(savedEngineer.getName());
    }

    @Test
    @DisplayName("Delete removes engineer when succesful")
    void delete_RemovesEngineer_WhenSuccessful(){
        Engineer engineerToBeSaved = EngineerCreator.createEngineerToBeSaved();
        Engineer savedEngineer = engineerRepository.save(engineerToBeSaved);
        engineerRepository.delete(savedEngineer);
        Optional<Engineer> engineerOptional = engineerRepository.findById(savedEngineer.getId());

        Assertions.assertThat(engineerOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of engineers when succesful")
    void findByName_ReturnsListOfEngineers_WhenSuccessful(){
        Engineer engineerToBeSaved = EngineerCreator.createEngineerToBeSaved();
        Engineer savedEngineer = engineerRepository.save(engineerToBeSaved);
        List<Engineer> engineers = engineerRepository.findByName(savedEngineer.getName());

        Assertions.assertThat(engineers).isNotEmpty().contains(savedEngineer);
    }

    @Test
    @DisplayName("Find by name returns empty list when no engineer is found")
    void findByName_ReturnsEmptyList_WhenEngineerNotFound(){
        List<Engineer> engineers = engineerRepository.findByName("notExistingName");

        Assertions.assertThat(engineers).isNotNull().isEmpty();//Returns a list object, that is empty but not null.
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        Engineer engineerToBeSaved = new Engineer();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> engineerRepository.save(engineerToBeSaved))
                .withMessageContaining("Engineer name cannot be empty");
    }
}