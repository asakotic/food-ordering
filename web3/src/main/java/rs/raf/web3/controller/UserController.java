package rs.raf.web3.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.AuthLoginDto;
import rs.raf.web3.model.dto.AuthResponse;
import rs.raf.web3.model.dto.ErrorDto;
import rs.raf.web3.service.ErrorService;
import rs.raf.web3.service.UserService;
import rs.raf.web3.configuration.anot.RequirePermission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private final ErrorService errorService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, ErrorService errorService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.errorService = errorService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    @RequirePermission("read")
    public ResponseEntity<List<User>> allUsers(@RequestHeader("Authorization") String authorization){
        try {
            List<User> users = userService.getAllUsers(authorization);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error fetching all users: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getId/{id}")
    @RequirePermission("read")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id, @RequestHeader("Authorization") String authorization){
        try {
            Optional<User> u = userService.findUserById(id, authorization);
            return u.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto( "Error fetching user by ID: " + e.getMessage(),LocalDateTime.now(),userEmail));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/{email}")
    @RequirePermission("read")
    public ResponseEntity<User> getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String authorization){
        try {
            Optional<User> u = userService.findUserByEmail(email, authorization);
            return u.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto( "Error fetching user by email: " + e.getMessage(),LocalDateTime.now(),userEmail));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/create")
    @RequirePermission("create")
    public ResponseEntity<User> createUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        try {
            if (userService.createUser(user, authorization)) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto( "Error creating user: " + e.getMessage(),LocalDateTime.now(),userEmail));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update")
    @RequirePermission("update")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        try {
            AuthResponse authResponse = userService.updateUser(user, authorization);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto( "Error updating user: " + e.getMessage(),LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete/{email}")
    @RequirePermission("delete")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email,@RequestHeader("Authorization") String authorization){
        try {
            userService.deleteUser(email, authorization);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto( "Error deleting user: " + e.getMessage(),LocalDateTime.now(),userEmail));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthLoginDto authLoginDto){
        try {
            AuthResponse response = userService.authenticate(authLoginDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
