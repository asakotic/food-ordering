package rs.raf.web3.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.Status;
import rs.raf.web3.model.dto.ErrorDto;
import rs.raf.web3.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatusUpdate {
    private final OrderRepository orderRepository;
    private final ErrorService errorService;

    public StatusUpdate(OrderRepository orderRepository, ErrorService errorService) {
        this.orderRepository = orderRepository;
        this.errorService = errorService;
    }

    @Scheduled(fixedRate = 5000) // 5 sekundi
    public void processOrderStatusUpdates() {
        LocalDateTime now = LocalDateTime.now();

        orderRepository.updateOrdersByStatusAndTime(
                Status.ORDERED,
                Status.PREPARING,
                now.minusSeconds(10)
        );

        orderRepository.updateOrdersByStatusAndTime(
                Status.PREPARING,
                Status.IN_DELIVERY,
                now.minusSeconds(15)
        );

        orderRepository.updateOrdersByStatusAndTime(
                Status.IN_DELIVERY,
                Status.DELIVERED,
                now.minusSeconds(20)
        );
        orderRepository.deactivateOrdersByStatus(Status.DELIVERED);
    }

    @Scheduled(fixedRate = 60000) //minutic
    public void scheduleOrderStatusUpdates() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> scheduledOrders = orderRepository.findOrdersByStatus(Status.SCHEDULED);

        for (Order order : scheduledOrders) {
            int userOrderedCount = orderRepository.countByUserAndStatusIn(order.getUser(), List.of(Status.ORDERED));

            if (userOrderedCount >= 3) {
                errorService.saveError(new ErrorDto("Error, cannot do scheduled order bcs 3 are already in order", LocalDateTime.now(),order.getUser().getEmail()));
                orderRepository.updateOrdersByStatusAndTime(
                        Status.SCHEDULED,
                        Status.CANCELED,
                        now
                );
            } else {
                orderRepository.updateOrdersByStatusAndTime(
                        Status.SCHEDULED,
                        Status.ORDERED,
                        now
                );
            }
        }
    }
}
