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
import project.house.builders.domain.Engineer;
import project.house.builders.requests.EngineerPostRequestBody;
import project.house.builders.requests.EngineerPutRequestBody;
import project.house.builders.service.EngineerService;
import project.house.builders.util.EngineerCreator;
import project.house.builders.util.EngineerPostRequestBodyCreator;
import project.house.builders.util.EngineerPutRequestBodyCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for engineer controller")
class EngineerControllerTest {
    @InjectMocks
    private EngineerController engineerController;
    @Mock
    private EngineerService engineerServiceMock;

    @BeforeEach
    void setUp(){
        List<Engineer> engineerList = new ArrayList<>(List.of(EngineerCreator.createValidEngineer()));

        BDDMockito.when(engineerServiceMock.listAll()).thenReturn(engineerList);
        BDDMockito.when(engineerServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong())).thenReturn(EngineerCreator.createValidEngineer());
        BDDMockito.when(engineerServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(engineerList);
        BDDMockito.when(engineerServiceMock.save(ArgumentMatchers.any(EngineerPostRequestBody.class))).thenReturn(EngineerCreator.createValidEngineer());
        BDDMockito.doNothing().when(engineerServiceMock).replace(ArgumentMatchers.any(EngineerPutRequestBody.class));
        BDDMockito.doNothing().when(engineerServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("listAll returns list of engineers when successful")
    void listAll_ReturnsListOfEngineer_WhenSuccessful(){
        String expectedName = EngineerCreator.createValidEngineer().getName();
        List<Engineer> engineerList = engineerController.listAll().getBody();

        Assertions.assertThat(engineerList).isNotNull();
        Assertions.assertThat(engineerList).isNotEmpty().hasSize(1);
        Assertions.assertThat(engineerList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns engineer when successful")
    void findById_ReturnsEngineer_WhenSuccessful(){
        Long expectedId = EngineerCreator.createValidEngineer().getId();
        Engineer engineer = engineerController.findById(1).getBody();

        Assertions.assertThat(engineer).isNotNull();
        Assertions.assertThat(engineer.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns list of engineer when successful")
    void findByName_ReturnsListOfEngineer_WhenSuccessful(){
        String expectedName = EngineerCreator.createValidEngineer().getName();
        List<Engineer> engineerList = engineerController.findByName("test").getBody();

        Assertions.assertThat(engineerList).isNotNull();
        Assertions.assertThat(engineerList).isNotEmpty().hasSize(1);
        Assertions.assertThat(engineerList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of engineer when engineer is not found")
    void findByName_ReturnsEmptyList_WhenEngineerIsNotFound(){
        BDDMockito.when(engineerServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when

        List<Engineer> engineerList = engineerController.findByName("test").getBody();

        Assertions.assertThat(engineerList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns engineer when successful")
    void save_ReturnsEngineer_WhenSuccessful(){
        Engineer engineer = engineerController.save(EngineerPostRequestBodyCreator.createEngineerPostRequestBody()).getBody();

        Assertions.assertThat(engineer).isNotNull().isEqualTo(EngineerCreator.createValidEngineer());
    }

    @Test
    @DisplayName("replace updates engineer when successful")
    void replace_UpdatesEngineer_WhenSuccessful(){
        Assertions.assertThatCode(() -> engineerController.replace(EngineerPutRequestBodyCreator.createEngineerPutRequestBody()))
                        .doesNotThrowAnyException();

        ResponseEntity<Void> entity = engineerController.replace(EngineerPutRequestBodyCreator.createEngineerPutRequestBody());

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes engineer when successful")
    void delete_RemovesEngineer_WhenSuccessful(){
        Assertions.assertThatCode(() -> engineerController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = engineerController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}