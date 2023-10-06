package project.house.builders.repository;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.house.builders.domain.Architect;
import project.house.builders.util.ArchitectCreator;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for architect repository")
class ArchitectRepositoryTest {
    @Autowired
    private ArchitectRepository architectRepository;

    @Test
    @DisplayName("Save persists architect when succesful")
    void save_PersistsArchitect_WhenSuccessful(){
        Architect architectToBeSaved = ArchitectCreator.createArchitectToBeSaved();
        Architect savedArchitect = architectRepository.save(architectToBeSaved);

        Assertions.assertThat(savedArchitect).isNotNull();
        Assertions.assertThat(savedArchitect.getId()).isNotNull();
        Assertions.assertThat(savedArchitect.getName()).isEqualTo(architectToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates architect when succesful")
    void save_UpdatesArchitect_WhenSuccessful(){
        Architect architectToBeSaved = ArchitectCreator.createArchitectToBeSaved();
        Architect savedArchitect = architectRepository.save(architectToBeSaved);
        savedArchitect.setName("New name");
        Architect architectUpdated = architectRepository.save(savedArchitect);

        Assertions.assertThat(architectUpdated).isNotNull();
        Assertions.assertThat(architectUpdated.getId()).isNotNull();
        Assertions.assertThat(architectUpdated.getName()).isEqualTo(savedArchitect.getName());
    }

    @Test
    @DisplayName("Delete removes architect when succesful")
    void delete_RemovesArchitect_WhenSuccessful(){
        Architect architectToBeSaved = ArchitectCreator.createArchitectToBeSaved();
        Architect savedArchitect = architectRepository.save(architectToBeSaved);
        architectRepository.delete(savedArchitect);
        Optional<Architect> ArchitectOptional = architectRepository.findById(savedArchitect.getId());

        Assertions.assertThat(ArchitectOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of architects when succesful")
    void findByName_ReturnsListOfArchitects_WhenSuccessful(){
        Architect architectToBeSaved = ArchitectCreator.createArchitectToBeSaved();
        Architect savedArchitect = architectRepository.save(architectToBeSaved);
        List<Architect> architects = architectRepository.findByName(savedArchitect.getName());

        Assertions.assertThat(architects).isNotEmpty().contains(savedArchitect);
    }

    @Test
    @DisplayName("Find by name returns empty list when no architect is found")
    void findByName_ReturnsEmptyList_WhenArchitectNotFound(){
        List<Architect> architects = architectRepository.findByName("notExistingName");

        Assertions.assertThat(architects).isNotNull().isEmpty();//Returns a list object, that is empty but not null.
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        Architect architectToBeSaved = new Architect();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> architectRepository.save(architectToBeSaved))
                .withMessageContaining("Architect name cannot be empty");
    }
}