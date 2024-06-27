package com.uade.tpo.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.config.JwtService;
import com.uade.tpo.ecommerce.entity.User;
import com.uade.tpo.ecommerce.entity.dto.AuthRequest;
import com.uade.tpo.ecommerce.entity.dto.AuthResponse;
import com.uade.tpo.ecommerce.entity.dto.SignupRequest;
import com.uade.tpo.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public User signup(SignupRequest request) {
    User user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role_id(request.getRole_id())
        .build();
    return repository.save(user);
  }

  public AuthResponse login(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));
    User user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    String accessToken = jwtService.generateToken(user);
    return AuthResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  public Long getLoggedUserId() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User loggedUser = (User) authentication.getPrincipal();
      return loggedUser.getId();
    } catch (Exception e) {
      return null;
    }

  }

}
