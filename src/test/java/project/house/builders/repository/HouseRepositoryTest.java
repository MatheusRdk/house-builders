package project.house.builders.repository;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.house.builders.domain.House;
import project.house.builders.util.HouseCreator;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Tests for house repository")
class HouseRepositoryTest {

    @Autowired
    private HouseRepository houseRepository;

    @Test
    @DisplayName("Save persists house when succesful")
    void save_PersistsHouse_WhenSuccessful(){
        House houseToBeSaved = HouseCreator.createHouseToBeSaved();
        House savedHouse = houseRepository.save(houseToBeSaved);

        Assertions.assertThat(savedHouse).isNotNull();
        Assertions.assertThat(savedHouse.getId()).isNotNull();
        Assertions.assertThat(savedHouse.getProjectName()).isEqualTo(houseToBeSaved.getProjectName());
    }

    @Test
    @DisplayName("Save updates house when succesful")
    void save_UpdatesHouse_WhenSuccessful(){
        House houseToBeSaved = HouseCreator.createHouseToBeSaved();
        House savedHouse = houseRepository.save(houseToBeSaved);
        savedHouse.setProjectName("New name");
        House houseUpdated = houseRepository.save(savedHouse);

        Assertions.assertThat(houseUpdated).isNotNull();
        Assertions.assertThat(houseUpdated.getId()).isNotNull();
        Assertions.assertThat(houseUpdated.getProjectName()).isEqualTo(savedHouse.getProjectName());
    }

    @Test
    @DisplayName("Delete removes house when succesful")
    void delete_RemovesHouse_WhenSuccessful(){
        House houseToBeSaved = HouseCreator.createHouseToBeSaved();
        House savedHouse = houseRepository.save(houseToBeSaved);
        houseRepository.delete(savedHouse);
        Optional<House> houseOptional = houseRepository.findById(savedHouse.getId());

        Assertions.assertThat(houseOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of houses when succesful")
    void findByName_ReturnsListOfHouses_WhenSuccessful(){
        House houseToBeSaved = HouseCreator.createHouseToBeSaved();
        House savedHouse = houseRepository.save(houseToBeSaved);
        List<House> houses = houseRepository.findByProjectName(savedHouse.getProjectName());

        Assertions.assertThat(houses).isNotEmpty().contains(savedHouse);
    }

    @Test
    @DisplayName("Find by name returns empty list when no house is found")
    void findByName_ReturnsEmptyList_WhenHouseNotFound(){
        List<House> houses = houseRepository.findByProjectName("notExistingName");

        Assertions.assertThat(houses).isNotNull().isEmpty();//Returns a list object, that is empty but not null.
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when projectName is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        House houseToBeSaved = new House();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> houseRepository.save(houseToBeSaved))
                .withMessageContaining("The house project must have a name or a nickname");
    }
}