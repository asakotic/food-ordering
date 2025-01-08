package rs.raf.web3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.configuration.anot.RequirePermission;
import rs.raf.web3.model.Order;
import rs.raf.web3.service.OrderService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @RequirePermission("order")
    public ResponseEntity<String> createOrder(@RequestBody Order order, @RequestHeader("Authorization") String authorization) {
        //try {
            orderService.createOrder(order, authorization);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        //} catch (OrderLimitExceededException e) {
          //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       // }*/
        //
        // return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }
    @PostMapping("/schedule")
    @RequirePermission("schedule")
    public ResponseEntity<String> scheduleOrder(@RequestBody Order order, @RequestHeader("Authorization") String authorization) {
        //try {
        orderService.scheduleOrder(order, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order scheduled successfully");
        //} catch (OrderLimitExceededException e) {
        //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        // }*/
        //
        // return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }

    @GetMapping("/search")
    @RequirePermission("search")
    public ResponseEntity<List<Order>> searchOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Long userId,
            @RequestHeader("Authorization") String authorization) {

        List<Order> orders = orderService.searchOrders(status, dateFrom, dateTo, userId, authorization);
        return ResponseEntity.ok(orders);
    }
    @PutMapping("/orders/cancel/{id}")
    @RequirePermission("cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            orderService.cancelOrder(id, authorization);
            return ResponseEntity.ok("Order cancelled successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
