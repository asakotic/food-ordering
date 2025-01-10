package rs.raf.web3.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.ErrorMessage;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.ErrorDto;
import rs.raf.web3.repository.ErrorRepository;
import rs.raf.web3.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class ErrorService {
    private final ErrorRepository errorRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ErrorService(ErrorRepository errorRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.errorRepository = errorRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Page<ErrorDto> getErrors(int page, int size, String authorization) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        User requestingUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(page, size);

        if (requestingUser.getAdmin()) {
            Page<ErrorMessage> errors = errorRepository.findAll(PageRequest.of(page, size));
            return errors.map(this::toDto);
        } else {
            Page<ErrorMessage> userErrors = errorRepository.findErrorMessageByUser_Id(requestingUser.getId(), pageable);
            return userErrors.map(this::toDto);
        }
    }
    public void saveError(ErrorDto error) {
        User user = userRepository.findUserByEmail(error.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ErrorMessage errorMessage = new ErrorMessage(user, error.getMessage(), error.getTimestamp());
        errorRepository.save(errorMessage);
    }

    private ErrorDto toDto(ErrorMessage error) {
        return new ErrorDto(error.getMessage(), error.getTimestamp(), error.getUser().getEmail());
    }
}
