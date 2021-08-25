package com.revilla.homestuff.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Consumption
 * @author Kirenai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nourishment"})
@Entity
@Table(name = "consumptions")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumption_id")
    private Long consumptionId;

    @Column(name = "unit")
    private Byte unit;

    @Column(name = "percentage", precision = 10, scale = 2)
    private BigDecimal percentage;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "nourishment_id", nullable = false)
    private Nourishment nourishment;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "consumptions")
    private Collection<User> users;

}
