package com.revilla.homestuff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.dto.only.RoleDtoOnly;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.*;

import java.util.Collection;

/**
 * RoleDto
 * @author Kirenai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto extends RoleDtoOnly {

    private Collection<UserDtoOnly> users;

}
