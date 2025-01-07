package rs.raf.web3.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.Status;
import rs.raf.web3.model.User;
import rs.raf.web3.repository.OrderRepository;
import rs.raf.web3.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createOrder(Order order, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7)); // Extract email from token
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int activeOrders = orderRepository.countByUserAndStatusIn(user, List.of(Status.ORDERED, Status.PREPARING, Status.IN_DELIVERY));
        if (activeOrders >= 3) {
          //  throw new OrderLimitExceededException("You cannot have more than 3 active orders.");
        }

        order.setUserId(user);
        order.setStatus(Status.ORDERED);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
