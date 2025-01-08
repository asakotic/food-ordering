package rs.raf.web3.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_dish")
@Data
public class OrderDish {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;
    @Column(nullable = false)
    private int quantity;
}
