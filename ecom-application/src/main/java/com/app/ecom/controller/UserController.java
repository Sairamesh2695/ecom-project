package com.app.ecom.controller;

import com.app.ecom.dto.user.UserReqDTO;
import com.app.ecom.dto.user.UserRespDTO;
import com.app.ecom.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserRespDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserReqDTO user){
        ResponseEntity.ok(userService.createUser(user));
        return ResponseEntity.ok("User created successfully.");
    }

    @GetMapping("/{userId}")
    public Optional<UserRespDTO> getUserById(@PathVariable int userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId,@RequestBody UserReqDTO user){
        return userService.updateUser(userId, user) ? ResponseEntity.ok("User updated successfully!") :
                ResponseEntity.badRequest().body("User couldn't be updated");
    }
}
