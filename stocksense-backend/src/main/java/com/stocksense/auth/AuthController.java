package com.stocksense.auth;
import com.stocksense.user.Role;
import com.stocksense.user.User;
import com.stocksense.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    private final Map<String, Otp> otps = new ConcurrentHashMap<>();

    // Request body for user registration
    record Register(
            @NotBlank String name,
            @Email String email,
            @Size(min = 8) String password,
            Role role
    ) {
    }

    // Request body for login
    record Login(
            @Email String email,
            @NotBlank String password
    ) {
    }

    // Response returned after successful authentication
    record Token(
            String token,
            String name,
            String email,
            Role role
    ) {
    }

    // Request body for forgot password
    record EmailReq(
            @Email String email
    ) {
    }

    // Request body for password reset
    record ResetReq(
            @Email String email,
            @Pattern(regexp = "\\d{6}") String otp,
            @Size(min = 8) String newPassword
    ) {
    }

    // Stores OTP and its expiration time
    record Otp(
            String code,
            Instant expires
    ) {
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody Register request
    ) {

        if (users.existsByEmailIgnoreCase(request.email())) {
            return ResponseEntity
                    .status(409)
                    .body(Map.of("message", "Email already registered"));
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(
                encoder.encode(request.password())
        );

        user.setRole(
                request.role() == null
                        ? Role.WAREHOUSE_STAFF
                        : request.role()
        );

        users.save(user);

        String token = jwt.generate(user.getEmail());

        return ResponseEntity.ok(
                new Token(
                        token,
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody Login request
    ) {

        return users
                .findByEmailIgnoreCase(request.email())
                .filter(user ->
                        encoder.matches(
                                request.password(),
                                user.getPasswordHash()
                        )
                )
                .<ResponseEntity<?>>map(user -> {

                    String token = jwt.generate(user.getEmail());

                    return ResponseEntity.ok(
                            new Token(
                                    token,
                                    user.getName(),
                                    user.getEmail(),
                                    user.getRole()
                            )
                    );
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(401)
                                .body(
                                        Map.of(
                                                "message",
                                                "Invalid credentials"
                                        )
                                )
                );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody EmailReq request
    ) {

        if (users.existsByEmailIgnoreCase(request.email())) {

            String otp = String.format(
                    "%06d",
                    new Random().nextInt(1_000_000)
            );

            String email = request.email().toLowerCase();

            otps.put(
                    email,
                    new Otp(
                            otp,
                            Instant.now().plusSeconds(300)
                    )
            );

            // Development only.
            // In production, send the OTP through an email service.
            System.out.println(
                    "StockSense DEV OTP for "
                            + request.email()
                            + ": "
                            + otp
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "If the email exists, an OTP was generated. "
                                + "In development, check backend console."
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetReq request
    ) {

        String email = request.email().toLowerCase();

        Otp otp = otps.get(email);

        if (otp == null
                || Instant.now().isAfter(otp.expires())
                || !otp.code().equals(request.otp())) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired OTP"
                            )
                    );
        }

        User user = users
                .findByEmailIgnoreCase(request.email())
                .orElseThrow();

        user.setPasswordHash(
                encoder.encode(request.newPassword())
        );

        users.save(user);

        // Remove OTP after successful password reset
        otps.remove(email);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password reset successful"
                )
        );
    }
}

