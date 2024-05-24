package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {


    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "College name cannot be a null value")
    private String college;

    @NotNull(message = "Email cannot be a null value")
    private String email;

}
