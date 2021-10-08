package com.revilla.homestuff.entity;

import java.util.Collection;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revilla.homestuff.util.enums.RoleName;
import lombok.*;
import org.hibernate.annotations.NaturalId;

/**
 * Role
 * @author Kirenai
 */
@Data
@Builder
@EqualsAndHashCode
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
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @NaturalId
    @Column(name = "name", nullable = false, length = 20)
    private RoleName name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

}
