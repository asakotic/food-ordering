package rs.raf.web3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.web3.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
