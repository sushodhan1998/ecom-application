package com.app.ecom.controller;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping()
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.fetchAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
    return userService.fetchUserBy(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping()
  public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
    userService.addUser(userRequest);
    return new ResponseEntity<>(
      "User added successfully",
      HttpStatus.CREATED
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateUser(
    @PathVariable Long id,
    @RequestBody UserRequest userRequest
  ) {
    Boolean result = userService.updateUser(id, userRequest);
    return result ?
      new ResponseEntity<>(
        "User updated Successfully",
        HttpStatus.CREATED
      ) : ResponseEntity.notFound().build();
  }
}
