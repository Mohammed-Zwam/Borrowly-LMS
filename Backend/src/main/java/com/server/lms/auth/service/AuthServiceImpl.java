package com.server.lms.auth.service;

import com.server.lms._shared.email.EmailService;
import com.server.lms._shared.email.EmailTemplate;
import com.server.lms.auth.dto.request.LoginRequest;
import com.server.lms.auth.dto.response.AuthResponse;
import com.server.lms.security.utils.JwtUtils;
import com.server.lms.user.dto.request.UserRequest;
import com.server.lms.user.entity.PasswordResetToken;
import com.server.lms.user.entity.User;
import com.server.lms.user.mapper.UserMapper;
import com.server.lms.user.repository.PasswordResetTokenRepository;
import com.server.lms.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.reset_password.url}")
    private String resetPasswordUrl;

    @Value("${spring.security.reset_password.expiration}")
    private Integer expiration;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        var user = userService.findEntityByEmail(loginRequest.getEmail());

        var lastLogin = LocalDateTime.now();
        userService.updateLastLogin(user.getId(), LocalDateTime.now());
        user.setLastLogin(lastLogin);

        var jwtToken = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .title("Welcome " + user.getName())
                .token(jwtToken)
                .user(userMapper.toDTO(user))
                .build();
    }

    @Override
    public AuthResponse signup(UserRequest userDTO) {

        User user = userService.create(userDTO);

        // user now authenticated, so not needed for apply filter chain :)

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .title("Welcome " + user.getName())
                .token(jwtToken)
                .user(userMapper.toDTO(user))
                .build();
    }

    @Override
    public void createPasswordResetToken(String email) {
        User user = userService.findEntityByEmail(email);


        String generatedToken = UUID.randomUUID().toString();
        var resetToken = PasswordResetToken.builder()
                .token(generatedToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(expiration + 2 /* SUPPOSE NETWORK DELAY */))
                .build();


        try {
            passwordResetTokenRepository.save(resetToken);
        } catch (Exception ex) {
            throw new RuntimeException("Failed To Save Token, " + ex.getMessage());
        }

        String resetLink = resetPasswordUrl + generatedToken;

        String emailSubject = "Password Reset Requested | Borrowly";

        Map<String, Object> variables = Map.of(
                "name", user.getName(),
                "resetLink", resetLink,
                "expiration", expiration
        );

        emailService.sendEmail(email, emailSubject, EmailTemplate.RESET_PASSWORD, variables);
    }

    @Override
    @Transactional // rollback when occur any exception
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).
                orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (passwordResetToken.isExpired()) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new RuntimeException("Token Expired, Please Request Again");
        }


        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.update(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }


}
