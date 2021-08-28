package com.revilla.homestuff.dto;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * NourishmentDto
 * @author Kirenai
 */
@Data
@ToString(exclude = {"user", "category"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NourishmentDto {

    private Long nourishmentId;

    private String name;

    private String imagePath;

    private String description;

    //private Boolean isAvailable;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserDto user;

    private CategoryDto category;

    private Collection<ConsumptionDto> consumptions;

    private AmountNourishementDto amountNourishment;

}
