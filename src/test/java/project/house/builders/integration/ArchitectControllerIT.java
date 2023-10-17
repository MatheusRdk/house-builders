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
import project.house.builders.domain.House;
import project.house.builders.repository.ArchitectRepository;
import project.house.builders.repository.HouseRepository;
import project.house.builders.requests.ArchitectPostRequestBody;
import project.house.builders.util.ArchitectCreator;
import project.house.builders.util.ArchitectPostRequestBodyCreator;
import project.house.builders.util.HouseCreator;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for architect controller")
class ArchitectControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ArchitectRepository architectRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Test
    @DisplayName("listAll returns list of architects when successful")
    void listAll_ReturnsListOfArchitect_WhenSuccessful() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());
        String expectedName = savedArchitect.getName();

        List<Architect> architects = testRestTemplate.exchange("/architects/all", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Architect>>() {
        }).getBody();

        Assertions.assertThat(architects).isNotNull();
        Assertions.assertThat(architects).isNotEmpty().hasSize(1);
        Assertions.assertThat(architects.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns architect when successful")
    void findById_ReturnsArchitect_WhenSuccessful() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());
        Long expectedId = savedArchitect.getId();

        Architect architect = testRestTemplate.exchange("/architects/{id}", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), Architect.class, expectedId).getBody();

        Assertions.assertThat(architect).isNotNull();
        Assertions.assertThat(architect.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns list of architect when successful")
    void findByName_ReturnsListOfArchitect_WhenSuccessful() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());
        String expectedName = savedArchitect.getName();
        String url = String.format("/architects/find?name=%s", expectedName);
        List<Architect> architects = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Architect>>() {
        }).getBody();

        Assertions.assertThat(architects).isNotNull();
        Assertions.assertThat(architects).isNotEmpty().hasSize(1);
        Assertions.assertThat(architects.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of architect when architect is not found")
    void findByName_ReturnsEmptyList_WhenArchitectIsNotFound() throws JSONException {
        List<Architect> architects = testRestTemplate.exchange("/architects/find?name=teste", HttpMethod.GET, new HttpEntity<>(getAdminHeader()), new ParameterizedTypeReference<List<Architect>>() {
        }).getBody();

        Assertions.assertThat(architects).isNotNull();
        Assertions.assertThat(architects).isEmpty();
    }

    @Test
    @DisplayName("save returns architect when successful")
    void save_ReturnsArchitect_WhenSuccessful() throws JSONException {
        ArchitectPostRequestBody postRequestBody = ArchitectPostRequestBodyCreator.createArchitectPostRequestBody();

        ResponseEntity<Architect> entity = testRestTemplate.exchange("/architects", HttpMethod.POST, new HttpEntity<>(postRequestBody, getAdminHeader()), new ParameterizedTypeReference<Architect>() {
        });

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(entity.getBody()).isNotNull();
        Assertions.assertThat(entity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates architect when successful")
    void replace_UpdatesArchitect_WhenSuccessful() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());
        savedArchitect.setName("new name");
        ResponseEntity<Void> entity = testRestTemplate.exchange("/architects", HttpMethod.PUT, new HttpEntity<>(savedArchitect, getAdminHeader()), Void.class);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes architect when successful")
    void delete_RemovesArchitect_WhenSuccessful() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());

        ResponseEntity<Void> entity = testRestTemplate.exchange("/architects/{id}", HttpMethod.DELETE, new HttpEntity<>(getAdminHeader()), Void.class, savedArchitect.getId());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete throws 403 when the architect is linked to a house")
    void delete_Throws403_WhenArchitectIsLinkedToHouse() throws JSONException {
        Architect savedArchitect = architectRepository.save(ArchitectCreator.createArchitectToBeSaved());
        House validHouse = HouseCreator.createValidHouse();
        validHouse.setArchitect(savedArchitect);
        houseRepository.save(validHouse);

        ResponseEntity<Void> entity = testRestTemplate.exchange("/architects/{id}", HttpMethod.DELETE, new HttpEntity<>(getAdminHeader()), Void.class, savedArchitect.getId());

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
