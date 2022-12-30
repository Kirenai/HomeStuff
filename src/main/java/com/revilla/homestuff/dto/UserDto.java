package com.revilla.homestuff.dto;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.dto.only.ConsumptionDtoOnly;
import com.revilla.homestuff.dto.only.NourishmentDtoOnly;
import com.revilla.homestuff.dto.only.RoleDtoOnly;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * UserDto
 *
 * @author Kirenai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends UserDtoOnly {

    private Collection<NourishmentDtoOnly> nourishments;

    private Collection<ConsumptionDtoOnly> consumptions;

    private Collection<RoleDtoOnly> roles;

    private String message;

    @Builder
    public UserDto(Long userId, @NotEmpty @Size(min = 2, max = 50) String username,
                   @NotEmpty @Size(min = 8, max = 64) String password,
                   @NotEmpty @Size(min = 2, max = 50) String firstName,
                   @NotEmpty @Size(min = 2, max = 50) String lastName,
                   @Min(value = 0) @Max(value = 100) Byte age,
                   Collection<NourishmentDtoOnly> nourishments,
                   Collection<ConsumptionDtoOnly> consumptions,
                   Collection<RoleDtoOnly> roles) {
        super(userId, username, password, firstName, lastName, age);
        this.nourishments = nourishments;
        this.consumptions = consumptions;
        this.roles = roles;
    }

    @Builder
    public UserDto(@NotEmpty @Size(min = 2, max = 50) String username,
                   @NotEmpty @Size(min = 8, max = 64) String password,
                   @NotEmpty @Size(min = 2, max = 50) String firstName,
                   @NotEmpty @Size(min = 2, max = 50) String lastName,
                   @Min(value = 0) @Max(value = 100) Byte age) {
        super(null, username, password, firstName, lastName, age);
    }

    /**
     * Adding a message to the response
     *
     * @param message of response
     * @return the object reference (this)
     */
    public UserDto setMessage(String message) {
        this.message = message;
        return this;
    }

}
