package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserResponse> fetchAllUsers() {
    return userRepository.findAll()
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  public void addUser(UserRequest userRequest) {
    User user = new User();
    updateRequestFromUser(user, userRequest);
    userRepository.save(user);
  }

  public Optional<UserResponse> fetchUserBy(Long id) {
    return userRepository.findById(id)
      .stream()
      .map(this::mapToResponse)
      .findFirst();
  }

  public Boolean updateUser(Long id, UserRequest modifiedUser) {
    return userRepository.findById(id)
      .map(exisitingUser -> {
        updateRequestFromUser(exisitingUser, modifiedUser);
        userRepository.save(exisitingUser);
        return true;
      }).orElse(false);
  }

  private UserResponse mapToResponse(User user) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(String.valueOf(user.getId()));
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getLastName());
    userResponse.setEmail(user.getEmail());
    userResponse.setPhone(user.getPhone());
    userResponse.setRole(user.getRole());

    if(Objects.nonNull(user.getAddress())){
      AddressDTO addressDTO = new AddressDTO();
      addressDTO.setCity(user.getAddress().getCity());
      addressDTO.setCountry(user.getAddress().getCountry());
      addressDTO.setStreet(user.getAddress().getStreet());
      addressDTO.setState(user.getAddress().getState());
      addressDTO.setZipcode(user.getAddress().getZipcode());
      userResponse.setAddress(addressDTO);
    }
    return userResponse;
  }

  private void updateRequestFromUser(User user, UserRequest userRequest) {
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());
    if(Objects.nonNull(userRequest.getAddress())){
      Address address = new Address();
      address.setCity(userRequest.getAddress().getCity());
      address.setCountry(userRequest.getAddress().getCountry());
      address.setStreet(userRequest.getAddress().getStreet());
      address.setZipcode(userRequest.getAddress().getZipcode());
      address.setState(userRequest.getAddress().getState());
      user.setAddress(address);
    }
  }
}
