package rs.raf.web3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    public Long id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public int price;

}
