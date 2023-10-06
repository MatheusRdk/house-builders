//package project.house.builders.integration;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.BDDMockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import project.house.builders.domain.House;
//import project.house.builders.repository.ArchitectRepository;
//import project.house.builders.repository.HouseRepository;
//import project.house.builders.util.HouseCreator;
//import project.house.builders.util.HousePostRequestBodyCreator;
//import project.house.builders.util.HousePutRequestBodyCreator;
//
//import java.util.Collections;
//import java.util.List;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase
//@DisplayName("Integration tests for house controller")
//class HouseControllerIT {
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Autowired
//    private HouseRepository houseRepository;
//
//
//    @Test
//    @DisplayName("listAll returns list of houses when successful")
//    void listAll_ReturnsListOfHouse_WhenSuccessful(){
//        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
//        String expectedName = savedHouse.getProjectName();
//
//        List<House> houses = testRestTemplate.exchange("/houses/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<House>>() {
//        }).getBody();
//
//        Assertions.assertThat(houses).isNotNull();
//        Assertions.assertThat(houses).isNotEmpty().hasSize(1);
//        Assertions.assertThat(houses.get(0).getProjectName()).isEqualTo(expectedName);
//    }
////
////    @Test
////    @DisplayName("FindById returns house when successful")
////    void findById_ReturnsHouse_WhenSuccessful(){
////        Long expectedId = HouseCreator.createValidHouse().getId();
////        House house = houseController.findById(1).getBody();
////
////        Assertions.assertThat(house).isNotNull();
////        Assertions.assertThat(house.getId()).isEqualTo(expectedId);
////    }
////
////    @Test
////    @DisplayName("FindByName returns list of house when successful")
////    void findByName_ReturnsListOfHouse_WhenSuccessful(){
////        String expectedName = HouseCreator.createValidHouse().getProjectName();
////        List<House> houseList = houseController.findByName("test").getBody();
////
////        Assertions.assertThat(houseList).isNotNull();
////        Assertions.assertThat(houseList).isNotEmpty().hasSize(1);
////        Assertions.assertThat(houseList.get(0).getProjectName()).isEqualTo(expectedName);
////    }
////
////    @Test
////    @DisplayName("FindByName returns an empty list of house when house is not found")
////    void findByName_ReturnsEmptyList_WhenHouseIsNotFound(){
////        BDDMockito.when(houseServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when
////
////        List<House> houseList = houseController.findByName("test").getBody();
////
////        Assertions.assertThat(houseList).isNotNull().isEmpty();
////    }
////
////    @Test
////    @DisplayName("save returns house when successful")
////    void save_ReturnsHouse_WhenSuccessful(){
////        House house = houseController.save(HousePostRequestBodyCreator.createHousePostRequestBodyOnlyWithName()).getBody();
////
////        Assertions.assertThat(house).isNotNull().isEqualTo(HouseCreator.createValidHouse());
////    }
////
////    @Test
////    @DisplayName("replace updates house when successful")
////    void replace_UpdatesHouse_WhenSuccessful(){
////        Assertions.assertThatCode(() -> houseController.replace(HousePutRequestBodyCreator.createHousePutRequestBodyOnlyWithName()))
////                .doesNotThrowAnyException();
////
////        ResponseEntity<Void> entity = houseController.replace(HousePutRequestBodyCreator.createHousePutRequestBody());
////
////        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
////    }
////
////    @Test
////    @DisplayName("delete removes house when successful")
////    void delete_RemovesHouse_WhenSuccessful(){
////        Assertions.assertThatCode(() -> houseController.delete(1))
////                .doesNotThrowAnyException();
////
////        ResponseEntity<Void> entity = houseController.delete(1);
////
////        Assertions.assertThat(entity).isNotNull();
////        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
////    }
//}
