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
public class CategoryDto {

    private Long categoryId;

    private String name;

    private Collection<NourishmentDto> nourishment;
}

