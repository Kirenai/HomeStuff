package com.revilla.homestuff.dto;

import java.math.BigDecimal;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.revilla.homestuff.entity.Nourishment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsumptionDto
 * @author Kirenai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumptionDto {

    private Long consumptionId;

    private Byte unit;

    private BigDecimal percentage;

    private Nourishment nourishment;

    private Collection<UserDto> users;

}
