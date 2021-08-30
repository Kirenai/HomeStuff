package com.revilla.homestuff.dto;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.dto.only.NourishmentDtoOnly;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.*;

/**
 * NourishmentDto
 * @author Kirenai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(exclude = {"user"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NourishmentDto extends NourishmentDtoOnly {

    private UserDtoOnly user;

    private Collection<ConsumptionDto> consumptions;

    private AmountNourishementDto amountNourishment;

}
