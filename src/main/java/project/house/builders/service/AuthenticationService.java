package project.house.builders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.house.builders.user.AuthenticationRequestBody;
import project.house.builders.user.RegisterRequestBody;
import project.house.builders.user.User;
import project.house.builders.user.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    public String authenticateAndGenerateToken(AuthenticationRequestBody data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login, data.password);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return token;
    }

    public ResponseEntity registerNewUser(RegisterRequestBody data){
        if(this.repository.findByLogin(data.login) != null) return ResponseEntity.badRequest().build();
        if(data.password.isEmpty()) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password);
        User newUser = new User(data.login, encryptedPassword, data.role);
        repository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
