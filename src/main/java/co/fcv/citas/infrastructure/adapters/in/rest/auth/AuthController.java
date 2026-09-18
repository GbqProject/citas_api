package co.fcv.citas.infrastructure.adapters.in.rest.auth;

import co.fcv.citas.application.auth.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController @Validated @RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    void register(@Valid @RequestBody RegisterRequest request) { service.register(new AuthService.RegisterCommand(request.firstName,request.lastName,request.documentType,request.documentNumber,request.email,request.phone,request.password)); }
    @PostMapping("/login") Tokens login(@Valid @RequestBody LoginRequest request) { return tokens(service.login(new AuthService.LoginCommand(request.email,request.password))); }
    @PostMapping("/refresh") Tokens refresh(@Valid @RequestBody RefreshRequest request) { return tokens(service.refresh(request.refreshToken)); }
    @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT) void logout(@Valid @RequestBody RefreshRequest request) { service.logout(request.refreshToken); }
    private Tokens tokens(AuthService.Tokens tokens) { return new Tokens(tokens.accessToken(),tokens.refreshToken(),"Bearer"); }
    record RegisterRequest(@NotBlank @Size(max=100) String firstName,@NotBlank @Size(max=100) String lastName,@NotBlank @Size(max=20) String documentType,@NotBlank @Size(max=30) String documentNumber,@NotBlank @Email @Size(max=254) String email,@NotBlank @Size(max=30) String phone,@NotBlank @Size(min=8,max=128) String password) { }
    record LoginRequest(@NotBlank @Email String email,@NotBlank String password) { }
    record RefreshRequest(@NotBlank String refreshToken) { }
    record Tokens(String accessToken,String refreshToken,String tokenType) { }
}
