package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeInterviewScheduleMail {

    private Employee employee;
    private Map<User, LocalDateTime> usersAndInterviewTime;

}
