package com.revilla.homestuff.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * AmountNourishementDto
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountNourishmentDto {

    private Byte unit;

    private BigDecimal percentage;

}
