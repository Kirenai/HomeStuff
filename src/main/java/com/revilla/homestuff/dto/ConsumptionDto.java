package com.revilla.homestuff.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ConsumptionDto
 * @author Kirenai
 */
@Data
@ToString(exclude = {"nourishment", "user"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumptionDto {

    private Long consumptionId;

    private Byte unit;

    private BigDecimal percentage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private NourishmentDto nourishment;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserDtoOnly user;

}
