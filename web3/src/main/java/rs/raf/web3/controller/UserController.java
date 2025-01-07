package rs.raf.web3.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.AuthLoginDto;
import rs.raf.web3.model.dto.AuthResponse;
import rs.raf.web3.service.UserService;
import rs.raf.web3.configuration.anot.RequirePermission;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @RequirePermission("read")
    public ResponseEntity<List<User>> allUsers(@RequestHeader("Authorization") String authorization){
        List<User> users = userService.getAllUsers(authorization);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/getId/{id}")
    @RequirePermission("read")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id, @RequestHeader("Authorization") String authorization){
        Optional<User> u = userService.findUserById(id,authorization);
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
    @GetMapping("/get/{email}")
    @RequirePermission("read")
    public ResponseEntity<User> getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String authorization){
        Optional<User> u = userService.findUserByEmail(email,authorization);
        if (u == null || u.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
    @PostMapping("/create")
    @RequirePermission("create")
    public ResponseEntity<User> createUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        if(userService.createUser(user,authorization))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PutMapping("/update")
    @RequirePermission("update")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        AuthResponse authResponse = userService.updateUser(user, authorization);
        return ResponseEntity.ok(authResponse);
    }
    @DeleteMapping("/delete/{email}")
    @RequirePermission("delete")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email,@RequestHeader("Authorization") String authorization){
        userService.deleteUser(email,authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthLoginDto authLoginDto){
        AuthResponse response = userService.authenticate(authLoginDto);
        return ResponseEntity.ok(response);
    }

}
