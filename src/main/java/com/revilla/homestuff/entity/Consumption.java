package com.revilla.homestuff.entity;

import java.math.BigDecimal;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

/**
 * Consumption
 *
 * @author Kirenai
 */
@Data
@Builder
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

    @Column(name = "percentage", precision = 3, scale = 2)
    private BigDecimal percentage;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nourishment_id", nullable = false)
    private Nourishment nourishment;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
