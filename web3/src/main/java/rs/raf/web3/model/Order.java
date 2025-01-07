package rs.raf.web3.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Status status = Status.SCHEDULED;

    @Column(nullable = false)
    private Date created_at;

    @Column(nullable = false)
    private Boolean active;
}
