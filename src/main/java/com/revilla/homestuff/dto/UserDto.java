package com.revilla.homestuff.dto;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revilla.homestuff.dto.only.ConsumptionDtoOnly;
import com.revilla.homestuff.dto.only.NourishmentDtoOnly;
import com.revilla.homestuff.dto.only.UserDtoOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends UserDtoOnly {

    private Collection<NourishmentDtoOnly> nourishments;

    private Collection<ConsumptionDtoOnly> consumptions;

}
