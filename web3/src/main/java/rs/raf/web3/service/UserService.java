package rs.raf.web3.service;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return null;
        else if (!u.get().getPermission().getRead()) {
            return null;
        }
        return userRepository.findAll();
    }
    public boolean createUser(User user,String authorization){
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return false;
        else if (!u.get().getPermission().getCreate()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public AuthResponse updateUser(User user, String authorization){
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return null;
        else if (!u.get().getPermission().getUpdate()) {
            return null;
        }
        Optional<User> user1 = userRepository.findUserById(user.getId());
        if(user1.isEmpty()) return null;
        User newUser = user1.get();
        Optional<User> tmp = userRepository.findUserByEmail(user.getEmail());
        User us = null;
        if(tmp.isPresent() && !user.getId().equals(tmp.get().getId()))
            return null;
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setPermission(user.getPermission());
        userRepository.save(newUser);
        return new AuthResponse(jwtUtil.generateToken(user));
    }
    public boolean deleteUser(String email, String authorization){
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return false;
        else if (!u.get().getPermission().getDelete()) {
            return false;
        }
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
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return null;
        else if (!(u.get().getPermission().getUpdate() && u.get().getPermission().getUpdate())) {
            return null;
        }
        return userRepository.findUserById(id);
    }
    public Optional<User> findUserByEmail(String email,String authorization){
        String token = authorization.substring(7);
        Optional<User> u = userRepository.findUserByEmail(jwtUtil.extractEmail(token));
        if(u.isEmpty()) return null;
        else if (!(u.get().getPermission().getUpdate() && u.get().getPermission().getUpdate())) {
            return null;
        }
        return userRepository.findUserByEmail(email);
    }
}
