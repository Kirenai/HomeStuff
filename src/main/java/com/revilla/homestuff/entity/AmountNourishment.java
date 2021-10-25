package com.revilla.homestuff.entity;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

/**
 * AmountNourishment
 * @author Kirenai
 */
@Data
@Builder
@ToString(exclude = {"nourishment"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amount_nourishment")
public class AmountNourishment {

    @Id
    @Column(name = "amountn_id")
    private Long amountNourishmentId;

    @Column(name = "unit")
    private Byte unit;

    @Column(name = "percentage", precision = 3, scale = 2)
    private BigDecimal percentage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(
            name = "amountn_id",
            foreignKey = @ForeignKey(name = "fk_nourishment_id")
    )
    private Nourishment nourishment;

}
