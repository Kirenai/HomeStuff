package com.revilla.homestuff.dto.only;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revilla.homestuff.dto.CategoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * NourishmentDto
 * @author Kirenai
 */
@Data
@SuperBuilder
@ToString(exclude = {"category"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NourishmentDtoOnly {

    private Long nourishmentId;

    @NotEmpty
    @Size(min = 2, max = 35)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 45)
    private String imagePath;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAvailable;

    private CategoryDto category;

}
