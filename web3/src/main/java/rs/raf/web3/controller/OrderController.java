package rs.raf.web3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.configuration.JwtUtil;
import rs.raf.web3.configuration.anot.RequirePermission;
import rs.raf.web3.model.Dish;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.dto.*;
import rs.raf.web3.service.ErrorService;
import rs.raf.web3.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ErrorService errorService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, ErrorService errorService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.errorService = errorService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    @RequirePermission("order")
    public ResponseEntity<String> createOrder(@RequestBody List<CreateOrderDto> dto, @RequestHeader("Authorization") String authorization) {
        try {
            orderService.createOrder(dto, authorization);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error creating order: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating order: " + e.getMessage());
        }
    }
    @PostMapping("/schedule")
    @RequirePermission("schedule")
    public ResponseEntity<String> scheduleOrder(@RequestBody ScheduleOrderRequest request,
                                                @RequestHeader("Authorization") String authorization) {
        try {
            orderService.scheduleOrder(request.getScheduledTime(), request.getDishOrderDtos(), authorization);
            return ResponseEntity.ok("Order scheduled successfully.");
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error scheduling order: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error scheduling order: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @RequirePermission("search")
    public ResponseEntity<List<OrderDto>> searchOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Long userId,
            @RequestHeader("Authorization") String authorization) {

        try{
            List<OrderDto> orders = orderService.searchOrders(status, dateFrom, dateTo,userId, authorization);
            return ResponseEntity.ok(orders);
        } catch (IllegalStateException e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error cancelling order: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }

    }
    @DeleteMapping("/cancel/{id}")
    @RequirePermission("cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            orderService.cancelOrder(id, authorization);
            return ResponseEntity.ok("Order cancelled successfully.");
        } catch (IllegalStateException e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error cancelling order: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error cancelling order: " + e.getMessage());
        }
    }

    @GetMapping("/dishes")
    @RequirePermission("order")
    public ResponseEntity<List<DishDto>> getDishes(@RequestHeader("Authorization") String authorization){
        try {
            List<DishDto> dishes = orderService.getDishes();
            return ResponseEntity.ok(dishes);
        } catch (Exception e) {
            String userEmail = jwtUtil.extractEmail(authorization.substring(7));
            errorService.saveError(new ErrorDto("Error fetching dishes: " + e.getMessage(), LocalDateTime.now(),userEmail));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
