package auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class UserController {

  private Map<String, User> users = new HashMap<>();

  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }

  @GetMapping("/userinfo")
  public ResponseEntity<User> getUserInfo(@RequestHeader("Session-Key") String sessionKey) {
      Optional<User> user = users.values().stream()
          .filter(u -> u.sessionKey.equals(sessionKey))
          .findFirst();

      if (user.isPresent()) {
          return ResponseEntity.ok(user.get());
      } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
  }

  @PostMapping("/changePassword")
  public ResponseEntity<String> changePassword(@RequestHeader("Reset-Key") String resetKey, @RequestBody Map<String, String> body) throws NoSuchAlgorithmException {
    Optional<User> user = users.values().stream()
      .filter(u -> u.resetKey.equals(resetKey))
      .findFirst();

    if (user.isPresent()) {
      String newPassword = body.get("newPassword");
      if (newPassword == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing newPassword in request body");
      }
      user.get().passwordHash = hashPassword(newPassword);
      return ResponseEntity.ok("Password changed successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/signup")
  public String signup(@RequestBody User user) throws NoSuchAlgorithmException {
    user.passwordHash = hashPassword(user.password);
    user.password = null;  // Delete the password from taken user object
    users.put(user.email, user);
    return "Signed up";
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) throws NoSuchAlgorithmException {
    User storedUser = users.get(user.email);
    if (storedUser != null && storedUser.passwordHash.equals(hashPassword(user.password))) {
      storedUser.sessionKey = generateSessionKey();
      return "Logged in. Session Key: " + storedUser.sessionKey;
    } else {
      return "Login failed.";
    }
  }

  @PostMapping("/resetPassword")
  public String resetPassword(@RequestBody User user) {
    Optional<User> storedUser = users.values().stream()
      .filter(u -> u.username.equals(user.username) && u.email.equals(user.email))
      .findFirst();

    if (storedUser.isPresent()) {
      storedUser.get().resetKey = generateSessionKey();
      return "Password reset. New Reset Key: " + storedUser.get().resetKey;
    } else {
      return "User not found.";
    }
  }

  private String hashPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(password.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : hash) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  @GetMapping("/users")
  public List<String> getUsers() {
    return users.values().stream()
      .map(user -> user.username)
      .collect(Collectors.toList());
  }

  private String generateSessionKey() {
    return UUID.randomUUID().toString();
  }

  static class User {
    public String username;
    public String email;
    public String password;
    public String passwordHash;
    public String sessionKey;
    public String resetKey;
  }
}