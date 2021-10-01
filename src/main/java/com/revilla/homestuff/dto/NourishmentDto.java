package com.revilla.homestuff.dto;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NourishmentDto extends NourishmentDtoOnly {

    private UserDtoOnly user;

    private Collection<ConsumptionDto> consumptions;

    @NotEmpty
    private AmountNourishementDto amountNourishment;

    private String message;

    /**
     * Adding a message to the response
     *
     * @param message of response
     * @return the object referece (this)
     */
    public NourishmentDto setMessage(String message) {
        this.message = message;
        return this;
    }

}
