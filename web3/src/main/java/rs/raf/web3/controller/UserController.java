package rs.raf.web3.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.AuthLoginDto;
import rs.raf.web3.model.dto.AuthResponse;
import rs.raf.web3.service.UserService;

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
    public ResponseEntity<List<User>> allUsers(@RequestHeader("Authorization") String authorization){
        List<User> u = userService.getAllUsers(authorization);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
    @GetMapping("/getId/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id, @RequestHeader("Authorization") String authorization){
        Optional<User> u = userService.findUserById(id,authorization);
        if (u == null || u.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
    @GetMapping("/get/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String authorization){
        Optional<User> u = userService.findUserByEmail(email,authorization);
        if (u == null || u.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        if(userService.createUser(user,authorization))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PutMapping("/update")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody User user,@RequestHeader("Authorization") String authorization){
        AuthResponse u = userService.updateUser(user,authorization);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email,@RequestHeader("Authorization") String authorization){
        if(userService.deleteUser(email,authorization))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthLoginDto authLoginDto){
        return new ResponseEntity<>(userService.authenticate(authLoginDto), HttpStatus.OK);
    }

}
