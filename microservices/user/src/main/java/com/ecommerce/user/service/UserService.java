package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserReqDTO;
import com.ecommerce.user.dto.UserRespDTO;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserRespDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToUserRespDTO)
                .collect(Collectors.toList());
    }

    public User createUser(UserReqDTO useRequest) {
        User user = new User();
        mapUserToUserReqDTO(useRequest,user);
        return userRepository.save(user);
    }

    public Optional<UserRespDTO> getUserById(int userId) {
        return userRepository.findById(userId).map(this::mapUserToUserRespDTO);
    }

    public boolean updateUser(Integer id, UserReqDTO updatedUser) {
        return userRepository.findById(id)
                .map(
                        existingUser ->{
                            mapUserToUserReqDTO(updatedUser,existingUser);
                            userRepository.save(existingUser);
                            return true;
                        }
                ).orElse(false);
    }

    public UserRespDTO mapUserToUserRespDTO(User user){
        UserRespDTO response = new UserRespDTO();

        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        if(user.getAddress() != null){
            AddressDTO address = new AddressDTO();
            address.setCity(user.getAddress().getCity());
            address.setState(user.getAddress().getState());
            address.setStreet(user.getAddress().getStreet());
            address.setCountry(user.getAddress().getCountry());
            address.setZip(user.getAddress().getZip());
            response.setAddress(address);
        }
        return response;
    }

    public void mapUserToUserReqDTO(UserReqDTO userRequest, User user){
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if(userRequest.getAddress() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setZip(userRequest.getAddress().getZip());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }
}
