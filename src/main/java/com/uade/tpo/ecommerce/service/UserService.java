package com.uade.tpo.ecommerce.service;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.entity.User;
import com.uade.tpo.ecommerce.entity.dto.UserRequest;
import com.uade.tpo.ecommerce.entity.dto.UserResponse;
import com.uade.tpo.ecommerce.exceptions.UserDuplicateException;
import com.uade.tpo.ecommerce.exceptions.UserNotFoundException;
import com.uade.tpo.ecommerce.exceptions.InsufficientPrivilegesException;
import com.uade.tpo.ecommerce.repository.UserRepository;

@Service
public class UserService implements IUserService {

  @Autowired
  private UserRepository repository;
  @Autowired
  private AuthService authService;

  public List<User> getAll() {
    return repository.findAll();
  }

  public List<User> getAllClients() {
    return repository.findAllClients();
  }

  public Optional<User> getById(Long id) {
    return repository.findById(id);
  }

  public User create(UserRequest request) throws UserDuplicateException {
    Optional<User> user = repository.findByEmail(request.getEmail());
    if (user.isEmpty()) {
      User newUser = new User(request);
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      String hashedPassword = passwordEncoder.encode(request.getPassword());
      newUser.setPassword(hashedPassword);
      return repository.save(newUser);
    }
    throw new UserDuplicateException();
  }

  public User update(UserRequest request) throws UserNotFoundException {
    Optional<User> user = repository.findById(request.getId());
    if (user.isPresent())
      return repository.save(new User(request));
    throw new UserNotFoundException();
  }

  public User getLoggedUser() {
    Long userId = authService.getLoggedUserId();
    if (userId != null) {
      Optional<User> user = repository.findById(userId);
      return user.get();
    }
    return null;
  }

  public static UserResponse userResponseBuilder(User a) {
    return UserResponse.builder()
        .id(a.getId())
        .firstname(a.getFirstname())
        .lastname(a.getLastname())
        .email(a.getEmail())
        .avatar_img(a.getAvatar_img())
        .role(a.getRole())
        .build();
  }

  public void deleteUserClient(Long id) throws UserNotFoundException, InsufficientPrivilegesException {
    Optional<User> user = repository.findById(id);
    if (user.isPresent()) {
      User foundUser = user.get();
      String roleName = foundUser.getRole().getName();
      if ("buyer".equals(roleName) || "seller".equals(roleName)) {
        repository.deleteById(id);
      } else {
        throw new InsufficientPrivilegesException();
      }
    } else {
      throw new UserNotFoundException();
    }
  }

}
