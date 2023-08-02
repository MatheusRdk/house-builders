package project.house.builders.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.request.HousePostRequestBodyCreator;
import project.house.builders.requests.HousePutRequestBody;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class HouseServiceTest {
    @InjectMocks
    private HouseService houseService;

    @Mock
    private HouseRepository houseRepository;

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

        BDDMockito.when(houseRepository.save(ArgumentMatchers.any(House.class)))
                .thenAnswer(invocation -> {
                    House houseToTest = invocation.getArgument(0);
                    houseToTest.setId(1L);
                    return houseToTest;
                });
    }

    @Test
    public void saveHouseWithEngineer_returnsHouse_WhenSuccessful(){

        Engineer existingEngineer = Engineer.builder().name("Existing Engineer").id(1L).houses(new ArrayList<>()).build();

        Mockito.when(engineerRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(existingEngineer));

        House savedHouse = houseService.save(HousePostRequestBodyCreator.createHousePostRequestBody());

        Mockito.verify(engineerRepository, Mockito.times(1)).findById(1L);

        Assertions.assertNotNull(savedHouse.getId());
        Assertions.assertEquals("TestProject", savedHouse.getProjectName());

        Mockito.verify(engineerRepository, Mockito.times(1)).findById(1L);

        Assertions.assertTrue(existingEngineer.getHouses().contains(savedHouse));
        Assertions.assertEquals(savedHouse.getEngineer(), existingEngineer);
    }

    @Test
    public void saveHouseWithArchitect_returnsHouse_WhenSuccessful(){

        Architect existingArchitect = Architect.builder().name("Existing Architect").id(1L).houses(new ArrayList<>()).build();

        Mockito.when(architectRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(existingArchitect));

        House savedHouse = houseService.save(HousePostRequestBodyCreator.createHousePostRequestBody());

        Mockito.verify(architectRepository, Mockito.times(1)).findById(1L);

        Assertions.assertNotNull(savedHouse.getId());
        Assertions.assertEquals("TestProject", savedHouse.getProjectName());

        Mockito.verify(architectRepository, Mockito.times(1)).findById(1L);

        Assertions.assertTrue(existingArchitect.getHouses().contains(savedHouse));
        Assertions.assertEquals(savedHouse.getArchitect(), existingArchitect);
    }

    @Test
    public void putHouse_withoutEngineerOrArchitect_shouldReturnEqualNameAndId_WhenSuccessful(){
        Long houseId = 1L;
        HousePutRequestBody requestBody = HousePutRequestBody.builder()
                .id(houseId)
                .projectName("UpdatedProject")
                .build();


        House existingHouse = House.builder()
                .id(houseId)
                .projectName("OriginalProject")
                .build();

        Mockito.when(houseRepository.findById(houseId)).thenReturn(Optional.of(existingHouse));
        Mockito.when(houseRepository.save(Mockito.any(House.class))).thenAnswer(invocation -> invocation.getArgument(0));

        houseService.replace(requestBody);

        // Then
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);
        Mockito.verify(houseRepository).save(houseCaptor.capture());
        House updatedHouse = houseCaptor.getValue();
        Assertions.assertEquals(requestBody.getId(), updatedHouse.getId());
        Assertions.assertEquals(requestBody.getProjectName(), updatedHouse.getProjectName());
    }
}
