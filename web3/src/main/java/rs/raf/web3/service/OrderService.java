package rs.raf.web3.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.Dish;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.Status;
import rs.raf.web3.model.User;
import rs.raf.web3.model.dto.DishOrderDto;
import rs.raf.web3.repository.DishRepository;
import rs.raf.web3.repository.OrderRepository;
import rs.raf.web3.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final JwtUtil jwtUtil;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, DishRepository dishRepository, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createOrder(List<DishOrderDto> dishOrderDtos, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7));
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int activeOrders = orderRepository.countByUserAndStatusIn(user, List.of(Status.ORDERED, Status.PREPARING, Status.IN_DELIVERY));
        if (activeOrders >= 3) {
          //  throw new OrderLimitExceededException("You cannot have more than 3 active orders.");
        }
        Order order = new Order();
        for (DishOrderDto dto : dishOrderDtos) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElseThrow(() -> new EntityNotFoundException("Dish not found with ID: " + dto.getDishId()));
            order.addDish(dish, dto.getQuantity());
        }
        order.setUser(user);
        order.setStatus(Status.ORDERED);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void scheduleOrder(LocalDateTime scheduledTime, List<DishOrderDto> dishOrderDtos, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7));
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setStatus(Status.SCHEDULED);
        order.setCreatedAt(scheduledTime);
        order.setUser(user);

        for (DishOrderDto dto : dishOrderDtos) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElseThrow(() -> new EntityNotFoundException("Dish not found"));
            order.addDish(dish, dto.getQuantity());
        }
        orderRepository.save(order);
    }

    public List<Order> searchOrders(String status, String dateFrom, String dateTo, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7)); // Extract email from JWT
        User requestingUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = requestingUser.getAdmin();
        if (isAdmin) {
            return orderRepository.searchAdvanced(status,LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo), null);
        }
        return orderRepository.searchAdvanced(status, LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo), requestingUser.getId());
    }

    public void cancelOrder(Long id, String authorization) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals(Status.ORDERED)) {
            throw new IllegalStateException("Orders can only be cancelled if they are in the ORDERED status.");
        }
        order.setStatus(Status.CANCELED);
        order.setActive(false);
        orderRepository.save(order);
    }
}
