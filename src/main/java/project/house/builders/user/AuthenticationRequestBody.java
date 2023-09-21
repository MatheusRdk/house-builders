package project.house.builders.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthenticationRequestBody {
    @Schema(description = "This is the login of your registered account", example = "user1")
    public String login;
    @Schema(description = "This is the password of your registered account", example = "pass123456")
    public String password;
}
