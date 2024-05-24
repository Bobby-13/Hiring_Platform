package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.dto.EmployeePaginationDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.util.enums.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Employee findEmployeeByEmployeeId(Long id);

    @Query("SELECT new com.divum.hiring_platform.dto.EmployeePaginationDto(employeeId, email, firstName, lastName, employeeType, stack, yearsOfExperience) FROM Employee ")
    Page<EmployeePaginationDto> getAllEmployees(Pageable pageable);

    @Query("SELECT new com.divum.hiring_platform.dto.EmployeePaginationDto(employeeId, email, firstName, lastName, employeeType, stack, yearsOfExperience) FROM Employee  WHERE employeeType = :type")
    Page<EmployeePaginationDto> getAllEmployeesByType(Pageable pageable, @Param("type") EmployeeType type);


    Employee findByEmail(String email);

    List<Employee> findEmployeesByEmployeeType(EmployeeType employeeType);
}
