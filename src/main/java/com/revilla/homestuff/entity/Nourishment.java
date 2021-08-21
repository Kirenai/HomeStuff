package com.revilla.homestuff.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Nourishment
 * @author Kirenai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "category"})
@Entity
@Table(name = "nourishments", uniqueConstraints = {
    @UniqueConstraint(name = "unq_name", columnNames = {"name"})
})
public class Nourishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nourishment_id")
    private Long nourishmentId;

    @Column(name = "name", nullable = false, length = 35)
    private String name;

    @Column(name = "image_path", nullable = false, length = 45)
    private String imagePath;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_id")
    )
    private User user;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "nourishment")
    private Collection<Consumption> consumptions;

}
