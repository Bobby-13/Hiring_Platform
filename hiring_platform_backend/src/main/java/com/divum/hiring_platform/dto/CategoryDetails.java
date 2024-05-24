package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDetails {

    private String heading;
    private Category category;
    private Map<String, Integer> categoryAndCount;
}
