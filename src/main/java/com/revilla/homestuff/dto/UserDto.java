package com.revilla.homestuff.dto;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.dto.only.ConsumptionDtoOnly;
import com.revilla.homestuff.dto.only.NourishmentDtoOnly;
import com.revilla.homestuff.dto.only.RoleDtoOnly;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * UserDto
 * @author Kirenai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends UserDtoOnly {

    private Collection<NourishmentDtoOnly> nourishments;

    private Collection<ConsumptionDtoOnly> consumptions;

    private Collection<RoleDtoOnly> roles;

    private String message;

    /**
     * Adding a message to the response
     *
     * @param message of response
     * @return the object referece (this)
     */
    public UserDto setMessage(String message) {
        this.message = message;
        return this;
    }

}
