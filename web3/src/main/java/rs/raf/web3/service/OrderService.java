package rs.raf.web3.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.model.*;
import rs.raf.web3.model.dto.*;
import rs.raf.web3.repository.DishRepository;
import rs.raf.web3.repository.OrderRepository;
import rs.raf.web3.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void createOrder(List<CreateOrderDto> dishOrderDtos, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7));
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int activeOrders = orderRepository.countByUserAndStatusIn(user, List.of(Status.ORDERED, Status.PREPARING, Status.IN_DELIVERY));
        if (activeOrders >= 3) {
            throw new RuntimeException("User cannot have more than 3 active orders.");
        }
        Order order = new Order();
        for (CreateOrderDto dto : dishOrderDtos) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElseThrow(() -> new EntityNotFoundException("Dish not found with ID: " + dto.getDishId()));
            order.addDish(dish, dto.getQuantity());
        }
        order.setUser(user);
        order.setStatus(Status.ORDERED);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
    public void scheduleOrder(LocalDateTime scheduledTime, List<CreateOrderDto> dishOrderDtos, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7));
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (scheduledTime == null || scheduledTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Scheduled time must be in the future.");
        }

        Order order = new Order();
        order.setStatus(Status.SCHEDULED);
        order.setCreatedAt(scheduledTime);
        order.setUser(user);

        List<String> invalidDishes = new ArrayList<>();
        for (CreateOrderDto dto : dishOrderDtos) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElse(null);
            if (dish == null) {
                invalidDishes.add("Dish with ID " + dto.getDishId() + " not found.");
                continue;
            }
            order.addDish(dish, dto.getQuantity());
        }

        if (!invalidDishes.isEmpty()) {
            throw new RuntimeException("Some dishes are invalid: " + String.join(", ", invalidDishes));
        }

        orderRepository.save(order);
    }

    public List<OrderDto> searchOrders(String status, String dateFrom, String dateTo, Long userId, String authorization) {
        String email = jwtUtil.extractEmail(authorization.substring(7));
        User requestingUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Status statusEnum = null;
        try {
            if (status != null && !status.equalsIgnoreCase("ALL")) {
                statusEnum = Status.valueOf(status.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value provided: " + status);
        }
        boolean isAdmin = requestingUser.getAdmin();
        LocalDateTime from = null;
        LocalDateTime to = null;
        try {
            if (dateFrom != null) from = LocalDateTime.parse(dateFrom);
            if (dateTo != null) to = LocalDateTime.parse(dateTo);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format.");
        }
        List<Order> orders;
        if (isAdmin) {
            orders = orderRepository.searchAdvanced(statusEnum, from, to, userId);
        } else {
            orders = orderRepository.searchAdvanced(statusEnum, from, to, requestingUser.getId());
        }

        return orders.stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setCreatedAt(order.getCreatedAt().toString());
            orderDto.setStatus(order.getStatus().toString());
            orderDto.setCreatedBy(order.getUser().getEmail());

            List<ResDishDto> dishes = order.getOrderDishes().stream()
                    .map(orderDish -> new ResDishDto(
                            orderDish.getDish().getName(),
                            orderDish.getQuantity(),
                            orderDish.getDish().getPrice() * orderDish.getQuantity()
                    ))
                    .collect(Collectors.toList());

            orderDto.setDishes(dishes);
            return new OrderDto(orderDto);
        }).collect(Collectors.toList());
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

    public List<DishDto> getDishes(){
        List<Dish>dishes =  dishRepository.findAll();
        List<DishDto> dishDtos = new ArrayList<>();
        for(Dish dish:dishes){
            dishDtos.add(new DishDto(dish.getId(),dish.getName(),dish.getPrice()));
        }
        return dishDtos;
    }
}
