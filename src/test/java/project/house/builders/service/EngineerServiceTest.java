package project.house.builders.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.house.builders.domain.Engineer;
import project.house.builders.exception.BadRequestException;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.util.EngineerCreator;
import project.house.builders.util.EngineerPostRequestBodyCreator;
import project.house.builders.util.EngineerPutRequestBodyCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for engineer service")
public class EngineerServiceTest {
    @InjectMocks
    private EngineerService engineerService;

    @Mock
    private EngineerRepository engineerRepositoryMock;


    @BeforeEach
    void setUp() {
        List<Engineer> engineerList = new ArrayList<>(List.of(EngineerCreator.createValidEngineer()));

        BDDMockito.when(engineerRepositoryMock.findAll()).thenReturn(engineerList);
        BDDMockito.when(engineerRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(EngineerCreator.createValidEngineer()));
        BDDMockito.when(engineerRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(engineerList);
        BDDMockito.when(engineerRepositoryMock.save(ArgumentMatchers.any(Engineer.class))).thenReturn(EngineerCreator.createValidEngineer());
        BDDMockito.doNothing().when(engineerRepositoryMock).delete(ArgumentMatchers.any(Engineer.class));
    }

    @Test
    @DisplayName("listAll returns list of engineers when successful")
    void listAll_ReturnsListOfEngineer_WhenSuccessful() {
        String expectedName = EngineerCreator.createValidEngineer().getName();
        List<Engineer> engineerList = engineerService.listAll();

        org.assertj.core.api.Assertions.assertThat(engineerList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(engineerList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(engineerList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns engineer when successful")
    void findByIdOrThrowBadRequestException_ReturnsEngineer_WhenSuccessful() {
        Long expectedId = EngineerCreator.createValidEngineer().getId();
        Engineer engineer = engineerService.findByIdOrThrowBadRequestException(1);

        org.assertj.core.api.Assertions.assertThat(engineer).isNotNull();
        org.assertj.core.api.Assertions.assertThat(engineer.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when engineer is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenEngineerIsNotFound() {
        BDDMockito.when(engineerRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> engineerService.findByIdOrThrowBadRequestException(1))
                .withMessageContaining("Engineer not found.");
    }

    @Test
    @DisplayName("FindByName returns list of engineer when successful")
    void findByName_ReturnsListOfEngineer_WhenSuccessful() {
        String expectedName = EngineerCreator.createValidEngineer().getName();
        List<Engineer> engineerList = engineerService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(engineerList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(engineerList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(engineerList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of engineer when engineer is not found")
    void findByName_ReturnsEmptyList_WhenEngineerIsNotFound() {
        BDDMockito.when(engineerRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when

        List<Engineer> engineerList = engineerService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(engineerList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns engineer when successful")
    void save_ReturnsEngineer_WhenSuccessful() {

        Engineer engineer = engineerService.save(EngineerPostRequestBodyCreator.createEngineerPostRequestBody());

        org.assertj.core.api.Assertions.assertThat(engineer).isNotNull().isEqualTo(EngineerCreator.createValidEngineer());
    }

    @Test
    @DisplayName("replace updates engineer when successful")
    void replace_UpdatesEngineer_WhenSuccessful() {
        org.assertj.core.api.Assertions.assertThatCode(() -> engineerService.replace(EngineerPutRequestBodyCreator.createEngineerPutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes engineer when successful")
    void delete_RemovesEngineer_WhenSuccessful() {
        org.assertj.core.api.Assertions.assertThatCode(() -> engineerService.delete(1))
                .doesNotThrowAnyException();
    }
}
