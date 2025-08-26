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
@RequestMapping( "/api/users" )
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping()
  public ResponseEntity<List<UserResponse>> getAllUser() {
    return ResponseEntity.ok( userService.fetchAllUserList() );
  }

  @GetMapping( "/{id}" )
  public ResponseEntity<UserResponse> getUser( @PathVariable Long id ) {
    return userService.getUserById( id )
      .map( ResponseEntity::ok )
      .orElseGet( () -> ResponseEntity.notFound().build() );
  }

  @PostMapping()
  public ResponseEntity<String> createUser( @RequestBody UserRequest creatUserRequest ) {
    userService.addUser( creatUserRequest );
    return new ResponseEntity<>( "User created successfully", HttpStatus.CREATED );
  }

  @PutMapping( "/{id}" )
  public ResponseEntity<String> updateUserRequest( @PathVariable Long id, @RequestBody UserRequest updateUserRequest ) {
    if ( userService.updateUser( id, updateUserRequest ) )
      return new ResponseEntity<>( "User updated successfully", HttpStatus.OK );
    return ResponseEntity.notFound().build();
  }
}
