package project.house.builders.integration;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import project.house.builders.domain.House;
import project.house.builders.repository.HouseRepository;
import project.house.builders.util.HouseCreator;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Integration tests for house controller")
class HouseControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private HouseRepository houseRepository;

    @Test
    @DisplayName("listAll returns list of houses when successful")
    void listAll_ReturnsListOfHouse_WhenSuccessful() throws JSONException {
        //Registration
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("login", "teste");
        registrationRequest.put("password", "teste");
        registrationRequest.put("role", "ADMIN");

        HttpHeaders registrationHeaders = new HttpHeaders();
        registrationHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registrationEntity = new HttpEntity<>(registrationRequest.toString(), registrationHeaders);
        ResponseEntity<String> registrationResponse = testRestTemplate.exchange("http://localhost:"+port+"/auth/register", HttpMethod.POST, registrationEntity, String.class);

        //Login
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("login", "teste");
        loginRequest.put("password", "teste");

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginRequest.toString(), loginHeaders);
        ResponseEntity<String> loginResponse = testRestTemplate.exchange("http://localhost:"+port+"/auth/login", HttpMethod.POST, loginEntity, String.class);

        //Getting the token
        JSONObject jsonResponse = new JSONObject(loginResponse.getBody());
        String token = jsonResponse.getString("token");

        //Create a header for requests
        HttpHeaders protectedEndpointHeaders = new HttpHeaders();
        protectedEndpointHeaders.setBearerAuth(token);

        List<House> houses = testRestTemplate.exchange("http://localhost:"+port+"/houses/all", HttpMethod.GET, new HttpEntity<>(protectedEndpointHeaders), new ParameterizedTypeReference<List<House>>() {
        }).getBody();

        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
        String expectedName = savedHouse.getProjectName();

        Assertions.assertThat(houses).isNotNull();
        Assertions.assertThat(houses).isNotEmpty().hasSize(1);
        Assertions.assertThat(houses.get(0).getProjectName()).isEqualTo(expectedName);
    }
//
//    @Test
//    @DisplayName("FindById returns house when successful")
//    void findById_ReturnsHouse_WhenSuccessful(){
//        Long expectedId = HouseCreator.createValidHouse().getId();
//        House house = houseController.findById(1).getBody();
//
//        Assertions.assertThat(house).isNotNull();
//        Assertions.assertThat(house.getId()).isEqualTo(expectedId);
//    }
//
//    @Test
//    @DisplayName("FindByName returns list of house when successful")
//    void findByName_ReturnsListOfHouse_WhenSuccessful(){
//        String expectedName = HouseCreator.createValidHouse().getProjectName();
//        List<House> houseList = houseController.findByName("test").getBody();
//
//        Assertions.assertThat(houseList).isNotNull();
//        Assertions.assertThat(houseList).isNotEmpty().hasSize(1);
//        Assertions.assertThat(houseList.get(0).getProjectName()).isEqualTo(expectedName);
//    }
//
//    @Test
//    @DisplayName("FindByName returns an empty list of house when house is not found")
//    void findByName_ReturnsEmptyList_WhenHouseIsNotFound(){
//        BDDMockito.when(houseServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList()); //Override the mockito.when
//
//        List<House> houseList = houseController.findByName("test").getBody();
//
//        Assertions.assertThat(houseList).isNotNull().isEmpty();
//    }
//
//    @Test
//    @DisplayName("save returns house when successful")
//    void save_ReturnsHouse_WhenSuccessful(){
//        House house = houseController.save(HousePostRequestBodyCreator.createHousePostRequestBodyOnlyWithName()).getBody();
//
//        Assertions.assertThat(house).isNotNull().isEqualTo(HouseCreator.createValidHouse());
//    }
//
//    @Test
//    @DisplayName("replace updates house when successful")
//    void replace_UpdatesHouse_WhenSuccessful(){
//        Assertions.assertThatCode(() -> houseController.replace(HousePutRequestBodyCreator.createHousePutRequestBodyOnlyWithName()))
//                .doesNotThrowAnyException();
//
//        ResponseEntity<Void> entity = houseController.replace(HousePutRequestBodyCreator.createHousePutRequestBody());
//
//        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//    }
//
//    @Test
//    @DisplayName("delete removes house when successful")
//    void delete_RemovesHouse_WhenSuccessful(){
//        Assertions.assertThatCode(() -> houseController.delete(1))
//                .doesNotThrowAnyException();
//
//        ResponseEntity<Void> entity = houseController.delete(1);
//
//        Assertions.assertThat(entity).isNotNull();
//        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//    }
}
