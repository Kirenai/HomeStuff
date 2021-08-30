package com.revilla.homestuff.entity;

import java.util.Collection;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User
 * @author Kirenai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nourishments", "consumptions"})
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unq_username", columnNames = {"username"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "age", nullable = false)
    private Byte age;

    @JsonProperty(access = Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Nourishment> nourishments;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @JsonProperty(access = Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    private Collection<Consumption> consumptions;

}
