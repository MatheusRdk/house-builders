package project.house.builders.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterRequestBody{

    @Schema(description = "This is the login of your account", example = "user1")
    public String login;
    @Schema(description = "This is the password of your account", example = "pass123456")
    public String password;
    @Schema(description = "This is the role of your account, can be USER or ADMIN", example = "USER")
    public UserRole role;
}
