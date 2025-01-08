package rs.raf.web3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.web3.model.Dish;

public interface DishRepository extends JpaRepository<Dish,Long> {
}
