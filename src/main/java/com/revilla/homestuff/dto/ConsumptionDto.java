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
@ToString(exclude = {"user"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumptionDto {

    private Long consumptionId;

    private Byte unit;

    private BigDecimal percentage;

    private UserDtoOnly user;

    private String message;

    /**
     * Adding a message to the response
     *
     * @param message of response
     * @return the object reference (this)
     */
    public ConsumptionDto setMessage(String message) {
        this.message = message;
        return this;
    }
}
