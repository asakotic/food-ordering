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

    @PostMapping()
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


}
