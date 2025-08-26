package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repostitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserResponse> fetchAllUserList() {
    return userRepository.findAll()
      .stream()
      .map( this::mapToUserResponse )
      .toList();
  }

  public Optional<UserResponse> getUserById( Long id ) {
    return userRepository
      .findById( id )
      .map( this::mapToUserResponse );
  }

  public void addUser( UserRequest userRequest ) {
    User user = new User();
    updateUserFromRequest( user, userRequest );
    userRepository.save( user );
  }

  public boolean updateUser( Long id, UserRequest updateUserRequest ) {
    return userRepository.findById( id ).map( existingUser -> {
      updateUserFromRequest( existingUser, updateUserRequest );
      userRepository.save( existingUser );
      return true;
    } ).orElse( false );

  }

  private UserResponse mapToUserResponse( User user ) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId( user.getId() );
    userResponse.setFirstName( user.getFirstName() );
    userResponse.setLastName( user.getLastName() );
    userResponse.setEmail( user.getEmail() );
    userResponse.setPhone( user.getPhone() );
    userResponse.setRole( user.getRole() );

    if ( Objects.nonNull( user.getAddress() ) ) {
      AddressDTO addressDTO = new AddressDTO();
      addressDTO.setStreet( user.getAddress().getStreet() );
      addressDTO.setCity( user.getAddress().getCity() );
      addressDTO.setState( user.getAddress().getState() );
      addressDTO.setCountry( user.getAddress().getCountry() );
      addressDTO.setZipcode( user.getAddress().getZipcode() );
      userResponse.setAddress( addressDTO );
    }
    return userResponse;
  }

  private User updateUserFromRequest( User user, UserRequest userRequest ) {
    user.setFirstName( userRequest.getFirstName() );
    user.setLastName( userRequest.getLastName() );
    user.setEmail( userRequest.getEmail() );
    user.setPhone( userRequest.getPhone() );
    if ( Objects.nonNull( userRequest.getAddress() ) ) {
      Address address = new Address();
      address.setStreet( userRequest.getAddress().getStreet() );
      address.setCity( userRequest.getAddress().getCity() );
      address.setState( userRequest.getAddress().getState() );
      address.setCountry( userRequest.getAddress().getCountry() );
      address.setZipcode( userRequest.getAddress().getZipcode() );
      user.setAddress( address );
    }
    return user;
  }
}
