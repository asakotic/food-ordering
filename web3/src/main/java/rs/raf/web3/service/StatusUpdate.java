package rs.raf.web3.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.raf.web3.model.Status;
import rs.raf.web3.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
public class StatusUpdate {
    private final OrderRepository orderRepository;

    public StatusUpdate(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

   // @Scheduled(fixedRate = 5000) // 5 sekundi
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

  //  @Scheduled(fixedRate = 60000) //minutic
    public void scheduleOrderStatusUpdates() {
        LocalDateTime now = LocalDateTime.now();
        orderRepository.updateOrdersByStatusAndTime(
                Status.SCHEDULED,
                Status.ORDERED,
                now
        );
    }
}
