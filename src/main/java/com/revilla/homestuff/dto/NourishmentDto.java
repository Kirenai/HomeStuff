package com.revilla.homestuff.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * NourishmentDto
 * @author Kirenai
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NourishmentDto {

    private Long nourishmentId;

    private String name;

    private String imagePath;

    private String description;

    private Boolean isAvailable;

    private UserDto user;

    private CategoryDto category;

    private Collection<ConsumptionDto> consumptions;

}
