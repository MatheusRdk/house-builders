package project.house.builders.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.house.builders.domain.Architect;
import project.house.builders.requests.ArchitectPostRequestBody;
import project.house.builders.requests.ArchitectPutRequestBody;
import project.house.builders.service.ArchitectService;
import project.house.builders.util.ArchitectCreator;
import project.house.builders.util.ArchitectPostRequestBodyCreator;
import project.house.builders.util.ArchitectPutRequestBodyCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for architect controller")
class ArchitectControllerTest {
    @InjectMocks
    private ArchitectController architectController;
    @Mock
    private ArchitectService architectServiceMock;

    @BeforeEach
    void setUp(){
        List<Architect> architectList = new ArrayList<>(List.of(ArchitectCreator.createValidArchitect()));

        BDDMockito.when(architectServiceMock.listAll()).thenReturn(architectList);
        BDDMockito.when(architectServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong())).thenReturn(ArchitectCreator.createValidArchitect());
        BDDMockito.when(architectServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(architectList);
        BDDMockito.when(architectServiceMock.save(ArgumentMatchers.any(ArchitectPostRequestBody.class))).thenReturn(ArchitectCreator.createValidArchitect());
        BDDMockito.doNothing().when(architectServiceMock).replace(ArgumentMatchers.any(ArchitectPutRequestBody.class));
        BDDMockito.doNothing().when(architectServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("listAll returns list of architects when successful")
    void listAll_ReturnsListOfArchitect_WhenSuccessful(){
        String expectedName = ArchitectCreator.createValidArchitect().getName();
        List<Architect> architectList = architectController.listAll().getBody();

        Assertions.assertThat(architectList).isNotNull();
        Assertions.assertThat(architectList).isNotEmpty().hasSize(1);
        Assertions.assertThat(architectList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns architect when successful")
    void findById_ReturnsArchitect_WhenSuccessful(){
        Long expectedId = ArchitectCreator.createValidArchitect().getId();
        Architect architect = architectController.findById(1).getBody();

        Assertions.assertThat(architect).isNotNull();
        Assertions.assertThat(architect.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns list of architect when successful")
    void findByName_ReturnsListOfArchitect_WhenSuccessful(){
        String expectedName = ArchitectCreator.createValidArchitect().getName();
        List<Architect> architectList = architectController.findByName("test").getBody();

        Assertions.assertThat(architectList).isNotNull();
        Assertions.assertThat(architectList).isNotEmpty().hasSize(1);
        Assertions.assertThat(architectList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of architect when architect is not found")
    void findByName_ReturnsEmptyList_WhenArchitectIsNotFound(){
        BDDMockito.when(architectServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when

        List<Architect> architectList = architectController.findByName("test").getBody();

        Assertions.assertThat(architectList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns architect when successful")
    void save_ReturnsArchitect_WhenSuccessful(){
        Architect architect = architectController.save(ArchitectPostRequestBodyCreator.createArchitectPostRequestBody()).getBody();

        Assertions.assertThat(architect).isNotNull().isEqualTo(ArchitectCreator.createValidArchitect());
    }

    @Test
    @DisplayName("replace updates architect when successful")
    void replace_UpdatesArchitect_WhenSuccessful(){
        Assertions.assertThatCode(() -> architectController.replace(ArchitectPutRequestBodyCreator.createArchitectPutRequestBody()))
                        .doesNotThrowAnyException();

        ResponseEntity<Void> entity = architectController.replace(ArchitectPutRequestBodyCreator.createArchitectPutRequestBody());

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes architect when successful")
    void delete_RemovesArchitect_WhenSuccessful(){
        Assertions.assertThatCode(() -> architectController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = architectController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}