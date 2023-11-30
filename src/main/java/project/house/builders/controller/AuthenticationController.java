package project.house.builders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.house.builders.service.AuthenticationService;
import project.house.builders.user.AuthenticationRequestBody;
import project.house.builders.user.LoginResponseBody;
import project.house.builders.user.RegisterRequestBody;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "You should copy the JWT token that will be displayed in the response body when logging successfuly.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequestBody data){
        String token = authenticationService.authenticateAndGenerateToken(data);
        return ResponseEntity.ok(new LoginResponseBody(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request, password cannot be null or empty, or login already exists"),
            @ApiResponse(responseCode = "403", description = "Check the role name, login and password cannot be null or empty.")
    })
    public ResponseEntity register(@RequestBody @Valid RegisterRequestBody data){
        return authenticationService.registerNewUser(data);
    }
}
