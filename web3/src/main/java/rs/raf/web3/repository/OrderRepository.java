package rs.raf.web3.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.Status;
import rs.raf.web3.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    int countByUserAndStatusIn(User user, List<Status> statuses);

        @Query("""
        SELECT o FROM Ord o
        WHERE (:status IS NULL OR o.status = :status)
        AND (:dateFrom IS NULL OR o.createdAt >= :dateFrom)
        AND (:dateTo IS NULL OR o.createdAt <= :dateTo)
        AND (:userId IS NULL OR o.user.id = :userId)
    """)
        List<Order> searchAdvanced(
                @Param("status") Status status,
                @Param("dateFrom") LocalDateTime dateFrom,
                @Param("dateTo") LocalDateTime dateTo,
                @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("""
        UPDATE Ord o
        SET o.status = :newStatus, o.createdAt = CURRENT_TIMESTAMP
        WHERE o.status = :currentStatus AND o.createdAt <= :time
    """)
    void updateOrdersByStatusAndTime(
            @Param("currentStatus") Status currentStatus,
            @Param("newStatus") Status newStatus,
            @Param("time") LocalDateTime time
    );
    @Transactional
    @Modifying
    @Query("UPDATE Ord o SET o.active = false WHERE o.status = :status AND o.active = true")
    void deactivateOrdersByStatus(@Param("status") Status status);

    List<Order> findOrdersByStatus(Status status);
}
