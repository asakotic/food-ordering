package rs.raf.web3.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import rs.raf.web3.configuration.anot.RequirePermission;
import rs.raf.web3.model.User;
import rs.raf.web3.repository.UserRepository;

import java.util.Optional;

@Aspect
@Component
public class PermissionAspect {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private HttpServletRequest httpServletRequest;

    public PermissionAspect(JwtUtil jwtUtil, UserRepository userRepository, HttpServletRequest httpServletRequest) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Before("@annotation(requirePermission)")
    public void checkPermission(RequirePermission requirePermission) {

        String requiredPermission = requirePermission.value();
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("No authorization token provided");
        }
        String token = authorizationHeader.substring(7);

        // Get the current user by email
        String email = jwtUtil.extractEmail(token);
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if (userOptional.isEmpty()) {
            throw new AccessDeniedException("User not found");
        }

        User user = userOptional.get();
        boolean hasPermission;
        switch (requiredPermission) {
            case "create":
                hasPermission = user.getPermission().getCreate();
                break;
            case "delete":
                hasPermission = user.getPermission().getDelete();
                break;
            case "read":
                hasPermission = user.getPermission().getRead();
                break;
            case "update":
                hasPermission = user.getPermission().getUpdate();
                break;
            case "search":
                hasPermission = user.getPermission().getSearch();
                break;
            case "order":
                hasPermission = user.getPermission().getOrder();
                break;
            case "cancel":
                hasPermission = user.getPermission().getCancel();
                break;
            case "track":
                hasPermission = user.getPermission().getTrack();
                break;
            case "schedule":
                hasPermission = user.getPermission().getSchedule();
                break;
            default:
                hasPermission = false;
                break;
        }

        if (!hasPermission) {
            throw new AccessDeniedException("Permission denied");
        }
    }
}