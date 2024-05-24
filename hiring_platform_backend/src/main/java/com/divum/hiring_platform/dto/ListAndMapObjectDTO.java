package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListAndMapObjectDTO {

    private List<User> users;
    private Map<String, String> emailAndError;
}
