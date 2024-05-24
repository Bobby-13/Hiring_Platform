package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.EmployeeType;
import com.divum.hiring_platform.util.enums.Role;
import com.divum.hiring_platform.util.enums.Stack;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "employee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {

    @Id
    private Long employeeId;

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_employee",
            joinColumns = {
                    @JoinColumn(name = "employee_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "contest_id")
            }
    )
    private Set<Contest> contest;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Interview> interviews;

    @Enumerated(EnumType.STRING)
    private Stack stack;

    private boolean isActive;

    private int yearsOfExperience;

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee other = (Employee) obj;
        return Objects.equals(employeeId, other.employeeId);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", employeeType=" + employeeType +
                ", stack=" + stack +
                ", isActive=" + isActive +
                ", yearsOfExperience=" + yearsOfExperience +
                '}';
    }
}

