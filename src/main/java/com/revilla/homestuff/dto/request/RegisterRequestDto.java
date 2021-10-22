package com.revilla.homestuff.dto.request;

import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
public class RegisterRequestDto extends UserDtoOnly {

    public RegisterRequestDto(@NotEmpty @Size(min = 2, max = 50) String username,
                              @NotEmpty @Size(min = 8, max = 64) String password,
                              @NotEmpty @Size(min = 2, max = 50) String firstName,
                              @NotEmpty @Size(min = 2, max = 50) String lastName,
                              @Min(value = 0) @Max(value = 100) Byte age) {
        super(null, username, password, firstName, lastName, age);
    }

}
