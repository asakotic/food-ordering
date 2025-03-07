package rs.raf.web3.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.AuthLoginDto;
import rs.raf.web3.model.dto.AuthResponse;
import rs.raf.web3.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(String authorization){
        return userRepository.findAll();
    }
    public boolean createUser(User user,String authorization){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public AuthResponse updateUser(User user, String authorization) {
        String token = authorization.substring(7);
        String emailFromToken = jwtUtil.extractEmail(token);
        User editor = userRepository.findUserByEmail(emailFromToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.findUserByEmail(user.getEmail())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
                });

        if (!editor.getAdmin() && !existingUser.getEmail().equals(emailFromToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this user");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setPermission(user.getPermission());

        userRepository.save(existingUser);
        return new AuthResponse(jwtUtil.generateToken(existingUser));
    }
    public boolean deleteUser(String email, String authorization){
        userRepository.deleteByEmail(email);
        return true;
    }

    public AuthResponse authenticate(AuthLoginDto authLoginDto){
        System.out.println(authLoginDto.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginDto.getEmail(),
                        authLoginDto.getPassword()
                )
        );
        System.out.println(authLoginDto.getPassword());
        User user = userRepository.findUserByEmail(authLoginDto.getEmail()).orElseThrow();
        return new AuthResponse(jwtUtil.generateToken(user));
    }

    public Optional<User> findUserById(Long id,String authorization){
        return userRepository.findUserById(id);
    }
    public Optional<User> findUserByEmail(String email,String authorization){
        return userRepository.findUserByEmail(email);
    }
}
