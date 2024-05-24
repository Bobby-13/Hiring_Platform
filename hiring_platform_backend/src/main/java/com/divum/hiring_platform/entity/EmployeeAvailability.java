package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.EmployeeResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee_availability")
@Entity
public class EmployeeAvailability {

    @EmbeddedId
    private EmployeeAndContest employeeAndContest;

    @Enumerated(EnumType.STRING)
    private EmployeeResponse response;
}
