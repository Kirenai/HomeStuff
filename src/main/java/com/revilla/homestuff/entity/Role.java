package com.revilla.homestuff.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"users"})
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "unq_name", columnNames = {"name"}) 
})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

}
