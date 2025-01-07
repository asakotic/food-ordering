package rs.raf.web3.model;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
public class ErrorMessage {
    @Id
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private Timestamp timestamp;
    @Column(nullable = false)
    private String description;
}
