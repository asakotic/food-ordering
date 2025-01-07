package rs.raf.web3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.web3.model.Order;
import rs.raf.web3.model.Status;
import rs.raf.web3.model.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    int countByUserAndStatusIn(User user, List<Status> statuses);
}
