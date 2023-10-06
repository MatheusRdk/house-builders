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
import project.house.builders.domain.Architect;
import project.house.builders.exception.BadRequestException;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.util.ArchitectCreator;
import project.house.builders.util.ArchitectPostRequestBodyCreator;
import project.house.builders.util.ArchitectPutRequestBodyCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for architect service")
public class ArchitectServiceTest {
    @InjectMocks
    private ArchitectService architectService;

    @Mock
    private ArchitectRepository architectRepositoryMock;


    @BeforeEach
    void setUp() {
        List<Architect> architectList = new ArrayList<>(List.of(ArchitectCreator.createValidArchitect()));

        BDDMockito.when(architectRepositoryMock.findAll()).thenReturn(architectList);
        BDDMockito.when(architectRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ArchitectCreator.createValidArchitect()));
        BDDMockito.when(architectRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(architectList);
        BDDMockito.when(architectRepositoryMock.save(ArgumentMatchers.any(Architect.class))).thenReturn(ArchitectCreator.createValidArchitect());
        BDDMockito.doNothing().when(architectRepositoryMock).delete(ArgumentMatchers.any(Architect.class));
    }

    @Test
    @DisplayName("listAll returns list of architects when successful")
    void listAll_ReturnsListOfArchitect_WhenSuccessful() {
        String expectedName = ArchitectCreator.createValidArchitect().getName();
        List<Architect> architectList = architectService.listAll();

        org.assertj.core.api.Assertions.assertThat(architectList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(architectList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(architectList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns architect when successful")
    void findByIdOrThrowBadRequestException_ReturnsArchitect_WhenSuccessful() {
        Long expectedId = ArchitectCreator.createValidArchitect().getId();
        Architect architect = architectService.findByIdOrThrowBadRequestException(1);

        org.assertj.core.api.Assertions.assertThat(architect).isNotNull();
        org.assertj.core.api.Assertions.assertThat(architect.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when architect is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenArchitectIsNotFound() {
        BDDMockito.when(architectRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> architectService.findByIdOrThrowBadRequestException(1))
                .withMessageContaining("Architect not found.");
    }

    @Test
    @DisplayName("FindByName returns list of architect when successful")
    void findByName_ReturnsListOfArchitect_WhenSuccessful() {
        String expectedName = ArchitectCreator.createValidArchitect().getName();
        List<Architect> architectList = architectService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(architectList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(architectList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(architectList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of architect when architect is not found")
    void findByName_ReturnsEmptyList_WhenArchitectIsNotFound() {
        BDDMockito.when(architectRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when

        List<Architect> architectList = architectService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(architectList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns architect when successful")
    void save_ReturnsArchitect_WhenSuccessful() {

        Architect architect = architectService.save(ArchitectPostRequestBodyCreator.createArchitectPostRequestBody());

        org.assertj.core.api.Assertions.assertThat(architect).isNotNull().isEqualTo(ArchitectCreator.createValidArchitect());
    }

    @Test
    @DisplayName("replace updates architect when successful")
    void replace_UpdatesArchitect_WhenSuccessful() {
        org.assertj.core.api.Assertions.assertThatCode(() -> architectService.replace(ArchitectPutRequestBodyCreator.createArchitectPutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes architect when successful")
    void delete_RemovesArchitect_WhenSuccessful() {
        org.assertj.core.api.Assertions.assertThatCode(() -> architectService.delete(1))
                .doesNotThrowAnyException();
    }
}
