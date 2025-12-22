package eind.trmsmo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eind.trmsmo.dto.LoginRequest;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.repository.PatientRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Patient> pOpt = patientRepository.findByEmail(loginRequest.getEmail());
            if (pOpt.isPresent()) {
                Patient p = pOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Успешный вход в систему");
                response.put("patientId", p.getId());
                response.put("email", p.getEmail());
                response.put("role", "USER");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Пользователь не найден");
            }
        } catch (AuthenticationException ex) {
            return ResponseEntity.badRequest().body("Неверный email или пароль");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Успешный выход из системы");
    }
}