package project.house.builders.integration;

import com.google.gson.Gson;
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
import org.springframework.test.annotation.DirtiesContext;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.mapper.HouseMapper;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.requests.HousePutRequestBody;
import project.house.builders.util.ArchitectCreator;
import project.house.builders.util.EngineerCreator;
import project.house.builders.util.HouseCreator;
import project.house.builders.util.HousePostRequestBodyCreator;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for house controller")
class HouseControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private EngineerRepository engineerRepository;

    @Autowired
    private ArchitectRepository architectRepository;


    @Test
    @DisplayName("listAll returns list of houses when successful")
    void listAll_ReturnsListOfHouse_WhenSuccessful() throws JSONException {
        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
        String expectedName = savedHouse.getProjectName();

        List<House> houses = testRestTemplate.exchange("/houses/all", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<House>>() {
        }).getBody();

        Assertions.assertThat(houses).isNotNull();
        Assertions.assertThat(houses).isNotEmpty().hasSize(1);
        Assertions.assertThat(houses.get(0).getProjectName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns house when successful")
    void findById_ReturnsHouse_WhenSuccessful() throws JSONException {
        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
        Long expectedId = savedHouse.getId();

        House house = testRestTemplate.exchange("/houses/{id}", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), House.class, expectedId).getBody();

        Assertions.assertThat(house).isNotNull();
        Assertions.assertThat(house.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns list of house when successful")
    void findByName_ReturnsListOfHouse_WhenSuccessful() throws JSONException {
        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
        String expectedName = savedHouse.getProjectName();
        String url = String.format("/houses/find?name=%s", expectedName);
        List<House> houses = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<House>>() {
        }).getBody();

        Assertions.assertThat(houses).isNotNull();
        Assertions.assertThat(houses).isNotEmpty().hasSize(1);
        Assertions.assertThat(houses.get(0).getProjectName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of house when house is not found")
    void findByName_ReturnsEmptyList_WhenHouseIsNotFound() throws JSONException {
        List<House> houses = testRestTemplate.exchange("/houses/find?name=teste", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<House>>() {
        }).getBody();

        Assertions.assertThat(houses).isNotNull();
        Assertions.assertThat(houses).isEmpty();
    }

    @Test
    @DisplayName("save returns house when successful")
    void save_ReturnsHouse_WhenSuccessful() throws JSONException {
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyOnlyWithName();

        ResponseEntity<House> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<House>() {
        });

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(entity.getBody()).isNotNull();
        Assertions.assertThat(entity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("save with engineer and architect returns house when successful")
    void saveHouseEngineerArchitect_ReturnsHouse_WhenSuccessful() throws JSONException {
        Engineer engineerSaved = engineerRepository.save(EngineerCreator.createValidEngineer());
        Architect architectSaved = architectRepository.save(ArchitectCreator.createValidArchitect());
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyEngineerAndArchitect();

        //Here we need to return in String format because Jackson can't deserialize the Engineer and Architect attributes of the House.
        //After that, I convert the string with Engineer and Architect IDs into a format that I can transform into a House object,
        //so that I can test the attributes that came from the JSON returned by the restTemplate call.
        ResponseEntity<String> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<String>() {
        });

        //stringHouse contains a string in json format that represents the house object
        String stringHouse = entity.getBody();
        House house = getHouse(stringHouse);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(stringHouse).contains("architect");
        Assertions.assertThat(stringHouse).contains("engineer");
        Assertions.assertThat(stringHouse).isNotNull();
        Assertions.assertThat(house.getId()).isNotNull();
        Assertions.assertThat(house.getEngineer()).isNotNull();
        Assertions.assertThat(house.getArchitect()).isNotNull();
        Assertions.assertThat(house.getArchitect().getId()).isEqualTo(ArchitectCreator.createValidArchitect().getId());
        Assertions.assertThat(house.getEngineer().getId()).isEqualTo(EngineerCreator.createValidEngineer().getId());
    }

    @Test
    @DisplayName("save with engineer returns house when successful")
    void saveHouseWithEngineer_ReturnsHouse_WhenSuccessful() throws JSONException {
        Engineer engineerSaved = engineerRepository.save(EngineerCreator.createValidEngineer());
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyEngineerOnly();
        ResponseEntity<String> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<String>() {
        });

        String stringHouse = entity.getBody();
        House house = getHouse(stringHouse);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(stringHouse).contains("architect");
        Assertions.assertThat(stringHouse).contains("engineer");
        Assertions.assertThat(stringHouse).isNotNull();
        Assertions.assertThat(house.getId()).isNotNull();
        Assertions.assertThat(house.getEngineer()).isNotNull();
        Assertions.assertThat(house.getEngineer().getId()).isEqualTo(EngineerCreator.createValidEngineer().getId());
        Assertions.assertThat(house.getArchitect()).isNull();
    }

    @Test
    @DisplayName("save with architect returns house when successful")
    void saveHouseWithArchitect_ReturnsHouse_WhenSuccessful() throws JSONException {
        Architect architectSaved = architectRepository.save(ArchitectCreator.createValidArchitect());
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyArchitectOnly();
        ResponseEntity<String> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<String>() {
        });

        String stringHouse = entity.getBody();
        House house = getHouse(stringHouse);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(stringHouse).contains("architect");
        Assertions.assertThat(stringHouse).contains("engineer");
        Assertions.assertThat(stringHouse).isNotNull();
        Assertions.assertThat(house.getId()).isNotNull();
        Assertions.assertThat(house.getArchitect()).isNotNull();
        Assertions.assertThat(house.getArchitect().getId()).isEqualTo(ArchitectCreator.createValidArchitect().getId());
        Assertions.assertThat(house.getEngineer()).isNull();
    }
    @Test
    @DisplayName("save return 403 when the house's architect is not found")
    void saveHouseWithArchitect_Returns403_WhenArchitectIsNotFound() throws JSONException {
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyArchitectOnly();
        ResponseEntity<String> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<String>() {
        });

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("save return 403 when the house's engineer is not found")
    void saveHouseWithEngineer_Returns403_WhenEngineerIsNotFound() throws JSONException {
        HousePostRequestBody postRequestBody = HousePostRequestBodyCreator.createHousePostRequestBodyEngineerOnly();
        ResponseEntity<String> entity = testRestTemplate.exchange("/houses", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<String>() {
        });

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates house when successful")
    void replace_UpdatesHouse_WhenSuccessful() throws JSONException {
        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());
        savedHouse.setProjectName("new name");
        ResponseEntity<Void> entity = testRestTemplate.exchange("/houses", HttpMethod.PUT, new HttpEntity<>(savedHouse, getAdminHeader()), Void.class);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes house when successful")
    void delete_RemovesHouse_WhenSuccessful() throws JSONException {
        House savedHouse = houseRepository.save(HouseCreator.createHouseToBeSaved());

        ResponseEntity<Void> entity = testRestTemplate.exchange("/houses/{id}", HttpMethod.DELETE, new HttpEntity<>(getAdminHeader()), Void.class, savedHouse.getId());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private HttpHeaders getAdminHeader() throws JSONException {
        //Registration
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("login", "teste");
        registrationRequest.put("password", "teste");
        registrationRequest.put("role", "ADMIN");

        HttpHeaders registrationHeaders = new HttpHeaders();
        registrationHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registrationEntity = new HttpEntity<>(registrationRequest.toString(), registrationHeaders);
        ResponseEntity<String> registrationResponse = testRestTemplate.exchange("/auth/register", HttpMethod.POST, registrationEntity, String.class);

        //Login
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("login", "teste");
        loginRequest.put("password", "teste");

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginRequest.toString(), loginHeaders);
        ResponseEntity<String> loginResponse = testRestTemplate.exchange("/auth/login", HttpMethod.POST, loginEntity, String.class);

        //Getting the token
        JSONObject jsonResponse = new JSONObject(loginResponse.getBody());
        String token = jsonResponse.getString("token");

        //Create a header for requests
        HttpHeaders protectedEndpointHeaders = new HttpHeaders();
        protectedEndpointHeaders.setBearerAuth(token);
        return protectedEndpointHeaders;
    }

    private static House getHouse(String entity) {
        entity = entity.replace("\"engineer\":", "\"engineerId\":");
        entity = entity.replace("\"architect\":", "\"architectId\":");
        Gson gson = new Gson();
        HousePutRequestBody putRequestBodyConverted = gson.fromJson(entity, HousePutRequestBody.class);
        return HouseMapper.INSTANCE.toHouseWithEngAndArch(putRequestBodyConverted);
    }
}
