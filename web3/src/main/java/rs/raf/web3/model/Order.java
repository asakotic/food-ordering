package rs.raf.web3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Ord")
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<OrderDish> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(Set<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
    }
}
