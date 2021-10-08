package com.revilla.homestuff.dto.only;

import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.util.enums.RoleName;
import lombok.*;

/**
 * RoleDtoOnly
 * @author Kirenai
 */
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDtoOnly {

    private Long roleId;

    @NotEmpty
    private RoleName name;

}
