package rs.raf.web3.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Getter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "create", column = @Column(name = "can_create",nullable = false)),
            @AttributeOverride(name = "read", column = @Column(name = "can_read",nullable = false)),
            @AttributeOverride(name = "update", column = @Column(name = "can_update",nullable = false)),
            @AttributeOverride(name = "delete", column = @Column(name = "can_delete",nullable = false))
    })
    private Permission permission;
    private Boolean admin = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
