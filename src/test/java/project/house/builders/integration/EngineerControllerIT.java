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
import org.springframework.test.annotation.DirtiesContext;
import project.house.builders.domain.Architect;
import project.house.builders.domain.Engineer;
import project.house.builders.domain.House;
import project.house.builders.repository.EngineerRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.requests.EngineerPostRequestBody;
import project.house.builders.util.ArchitectCreator;
import project.house.builders.util.EngineerCreator;
import project.house.builders.util.EngineerPostRequestBodyCreator;
import project.house.builders.util.HouseCreator;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for engineer controller")
class EngineerControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private EngineerRepository engineerRepository;
    @Autowired
    private HouseRepository houseRepository;

    @Test
    @DisplayName("listAll returns list of engineers when successful")
    void listAll_ReturnsListOfEngineer_WhenSuccessful() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());
        String expectedName = savedEngineer.getName();

        List<Engineer> engineers = testRestTemplate.exchange("/engineers/all", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Engineer>>() {
        }).getBody();

        Assertions.assertThat(engineers).isNotNull();
        Assertions.assertThat(engineers).isNotEmpty().hasSize(1);
        Assertions.assertThat(engineers.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns engineer when successful")
    void findById_ReturnsEngineer_WhenSuccessful() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());
        Long expectedId = savedEngineer.getId();

        Engineer engineer = testRestTemplate.exchange("/engineers/{id}", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), Engineer.class, expectedId).getBody();

        Assertions.assertThat(engineer).isNotNull();
        Assertions.assertThat(engineer.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns list of engineer when successful")
    void findByName_ReturnsListOfEngineer_WhenSuccessful() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());
        String expectedName = savedEngineer.getName();
        String url = String.format("/engineers/find?name=%s", expectedName);
        List<Engineer> engineers = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Engineer>>() {
        }).getBody();

        Assertions.assertThat(engineers).isNotNull();
        Assertions.assertThat(engineers).isNotEmpty().hasSize(1);
        Assertions.assertThat(engineers.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of engineer when engineer is not found")
    void findByName_ReturnsEmptyList_WhenEngineerIsNotFound() throws JSONException {
        List<Engineer> engineers = testRestTemplate.exchange("/engineers/find?name=teste", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Engineer>>() {
        }).getBody();

        Assertions.assertThat(engineers).isNotNull();
        Assertions.assertThat(engineers).isEmpty();
    }

    @Test
    @DisplayName("save returns engineer when successful")
    void save_ReturnsEngineer_WhenSuccessful() throws JSONException {
        EngineerPostRequestBody postRequestBody = EngineerPostRequestBodyCreator.createEngineerPostRequestBody();

        ResponseEntity<Engineer> entity = testRestTemplate.exchange("/engineers", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<Engineer>() {
        });

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(entity.getBody()).isNotNull();
        Assertions.assertThat(entity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates engineer when successful")
    void replace_UpdatesEngineer_WhenSuccessful() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());
        savedEngineer.setName("new name");
        ResponseEntity<Void> entity = testRestTemplate.exchange("/engineers", HttpMethod.PUT, new HttpEntity<>(savedEngineer, getAdminHeader()), Void.class);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes engineer when successful")
    void delete_RemovesEngineer_WhenSuccessful() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());

        ResponseEntity<Void> entity = testRestTemplate.exchange("/engineers/{id}", HttpMethod.DELETE, new HttpEntity<>(getAdminHeader()), Void.class, savedEngineer.getId());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete throws 403 when the engineer is linked to a house")
    void delete_Throws403_WhenEngineerIsLinkedToHouse() throws JSONException {
        Engineer savedEngineer = engineerRepository.save(EngineerCreator.createEngineerToBeSaved());
        House validHouse = HouseCreator.createValidHouse();
        validHouse.setEngineer(savedEngineer);
        houseRepository.save(validHouse);

        ResponseEntity<Void> entity = testRestTemplate.exchange("/architects/{id}", HttpMethod.DELETE, new HttpEntity<>(getAdminHeader()), Void.class, savedEngineer.getId());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
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
}
