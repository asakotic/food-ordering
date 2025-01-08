package rs.raf.web3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(nullable = false)
    private Status status = Status.SCHEDULED;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDish> orderDishes = new HashSet<>();

    public void addDish(Dish dish, int quantity) {
        OrderDish orderDish = new OrderDish();
        orderDish.setOrder(this);
        orderDish.setDish(dish);
        orderDish.setQuantity(quantity);
        orderDishes.add(orderDish);
    }

    public void removeDish(Dish dish) {
        orderDishes.removeIf(od -> od.getDish().equals(dish));
    }
}
