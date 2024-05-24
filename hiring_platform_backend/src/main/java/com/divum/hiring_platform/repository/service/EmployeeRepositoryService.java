package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.dto.EmployeePaginationDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.util.enums.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepositoryService {

 

    Employee findEmployeeByEmployeeId(Long employeeId);


    void save(Employee employee1);


    Employee findEmployeeByEmail(String emailId);

    Optional<Employee> findById(Long employeeId);



    Page<EmployeePaginationDto> getAllEmployees(Pageable pageable);

    Employee findEmployeesByEmployeeId(Long employeeId);

    Page<EmployeePaginationDto> getAllEmployeesByType(Pageable pageable, EmployeeType type);
    
    Employee findByEmployeeByEmployeeId(Long id);

    Employee findByEmail(String email);

    List<Employee> findEmployeesByEmployeeType(EmployeeType employeeType);
}
