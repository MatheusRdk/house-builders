package project.house.builders.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.exception.BadRequestException;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.requests.HousePutRequestBody;
import project.house.builders.util.HouseCreator;
import project.house.builders.util.HousePostRequestBodyCreator;
import project.house.builders.util.HousePutRequestBodyCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for house service")
public class HouseServiceTest {
    @InjectMocks
    private HouseService houseService;

    @Mock
    private HouseRepository houseRepositoryMock;

    @Mock
    private EngineerRepository engineerRepository;

    @Mock
    private ArchitectRepository architectRepository;

    @Mock
    private EngineerService engineerService;

    @Mock
    private ArchitectService architectService;

    @BeforeEach
    void setUp(){
        List<House> houseList = new ArrayList<>(List.of(HouseCreator.createValidHouse()));

        BDDMockito.when(houseRepositoryMock.findAll()).thenReturn(houseList);
        BDDMockito.when(houseRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(HouseCreator.createValidHouse()));
        BDDMockito.when(houseRepositoryMock.findByProjectName(ArgumentMatchers.anyString())).thenReturn(houseList);
        BDDMockito.when(houseRepositoryMock.save(ArgumentMatchers.any(House.class))).thenReturn(HouseCreator.createValidHouse());
        BDDMockito.doNothing().when(houseRepositoryMock).delete(ArgumentMatchers.any(House.class));
    }

    @Test
    public void saveHouseWithEngineer_returnsHouse_WhenSuccessful(){
        BDDMockito.when(houseRepositoryMock.save(ArgumentMatchers.any(House.class)))
                .thenAnswer(invocation -> {
                    House houseToTest = invocation.getArgument(0);
                    houseToTest.setId(1L);
                    return houseToTest;
                });

        Engineer existingEngineer = Engineer.builder().name("Existing Engineer").id(1L).houses(new ArrayList<>()).build();
        Architect existingArchitect = Architect.builder().name("Existing Architect").id(1L).houses(new ArrayList<>()).build();


        Mockito.when(engineerRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(existingEngineer));
        Mockito.when(architectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(existingArchitect));

        House savedHouse = houseService.save(HousePostRequestBodyCreator.createHousePostRequestBodyEngineerAndArchitect());

        Mockito.verify(engineerRepository, Mockito.times(1)).findById(1L);

        Assertions.assertNotNull(savedHouse.getId());
        Assertions.assertEquals("TestProject", savedHouse.getProjectName());

        Mockito.verify(engineerRepository, Mockito.times(1)).findById(1L);

        Assertions.assertTrue(existingEngineer.getHouses().contains(savedHouse));
        Assertions.assertEquals(savedHouse.getEngineer(), existingEngineer);
    }

    @Test
    public void putHousewithoutEngineerOrArchitect_shouldReturnEqualNameAndId_WhenSuccessful(){
        BDDMockito.when(houseRepositoryMock.save(ArgumentMatchers.any(House.class)))
                .thenAnswer(invocation -> {
                    House houseToTest = invocation.getArgument(0);
                    houseToTest.setId(1L);
                    return houseToTest;
                });

        Long houseId = 1L;
        HousePutRequestBody requestBody = HousePutRequestBody.builder()
                .id(houseId)
                .projectName("UpdatedProject")
                .build();


        House existingHouse = House.builder()
                .id(houseId)
                .projectName("OriginalProject")
                .build();

        Mockito.when(houseRepositoryMock.findById(houseId)).thenReturn(Optional.of(existingHouse));
        Mockito.when(houseRepositoryMock.save(Mockito.any(House.class))).thenAnswer(invocation -> invocation.getArgument(0));

        houseService.replace(requestBody);

        // Then
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);
        Mockito.verify(houseRepositoryMock).save(houseCaptor.capture());
        House updatedHouse = houseCaptor.getValue();
        Assertions.assertEquals(requestBody.getId(), updatedHouse.getId());
        Assertions.assertEquals(requestBody.getProjectName(), updatedHouse.getProjectName());
    }


    @Test
    @DisplayName("listAll returns list of houses when successful")
    void listAll_ReturnsListOfHouse_WhenSuccessful(){
        String expectedName = HouseCreator.createValidHouse().getProjectName();
        List<House> houseList = houseService.listAll();

        org.assertj.core.api.Assertions.assertThat(houseList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(houseList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(houseList.get(0).getProjectName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns house when successful")
    void findByIdOrThrowBadRequestException_ReturnsHouse_WhenSuccessful(){
        Long expectedId = HouseCreator.createValidHouse().getId();
        House house = houseService.findByIdOrThrowBadRequestException(1);

        org.assertj.core.api.Assertions.assertThat(house).isNotNull();
        org.assertj.core.api.Assertions.assertThat(house.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when house is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenHouseIsNotFound(){
        BDDMockito.when(houseRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> houseService.findByIdOrThrowBadRequestException(1))
                .withMessageContaining("House project not found.");
    }

    @Test
    @DisplayName("FindByName returns list of house when successful")
    void findByName_ReturnsListOfHouse_WhenSuccessful(){
        String expectedName = HouseCreator.createValidHouse().getProjectName();
        List<House> houseList = houseService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(houseList).isNotNull();
        org.assertj.core.api.Assertions.assertThat(houseList).isNotEmpty().hasSize(1);
        org.assertj.core.api.Assertions.assertThat(houseList.get(0).getProjectName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of house when house is not found")
    void findByName_ReturnsEmptyList_WhenHouseIsNotFound(){
        BDDMockito.when(houseRepositoryMock.findByProjectName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when

        List<House> houseList = houseService.findByName("test");

        org.assertj.core.api.Assertions.assertThat(houseList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns house when successful")
    void save_ReturnsHouse_WhenSuccessful(){
        BDDMockito.when(houseRepositoryMock.save(ArgumentMatchers.any(House.class)))
                .thenAnswer(invocation -> {
                    House houseToTest = invocation.getArgument(0);
                    houseToTest.setId(1L);
                    return houseToTest;
                });

        House house = houseService.save(HousePostRequestBodyCreator.createHousePostRequestBodyOnlyWithName());

        org.assertj.core.api.Assertions.assertThat(house).isNotNull().isEqualTo(HouseCreator.createValidHouse());
    }

    @Test
    @DisplayName("replace updates house when successful")
    void replace_UpdatesHouse_WhenSuccessful(){
        org.assertj.core.api.Assertions.assertThatCode(() -> houseService.replace(HousePutRequestBodyCreator.createHousePutRequestBodyOnlyWithName()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes house when successful")
    void delete_RemovesHouse_WhenSuccessful(){
        org.assertj.core.api.Assertions.assertThatCode(() -> houseService.delete(1))
                .doesNotThrowAnyException();
    }
}
