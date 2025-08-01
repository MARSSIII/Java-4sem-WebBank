package Controllers;

import Services.AuthService;

import lombok.RequiredArgsConstructor;

import models.requests.LoginRequest;
import models.requests.LogoutRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String jwtResponse = authService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.getToken());
        return ResponseEntity.ok("Successfully logged out. Token removed.");
    }
}