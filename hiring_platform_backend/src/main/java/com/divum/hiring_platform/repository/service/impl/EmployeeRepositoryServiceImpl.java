package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.dto.EmployeePaginationDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.repository.EmployeeRepository;
import com.divum.hiring_platform.repository.service.EmployeeRepositoryService;
import com.divum.hiring_platform.util.enums.EmployeeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmployeeRepositoryServiceImpl implements EmployeeRepositoryService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Page<EmployeePaginationDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.getAllEmployees(pageable);
    }


    @Override
    public Employee findEmployeeByEmployeeId(Long employeeId) {
        return employeeRepository.findEmployeeByEmployeeId(employeeId);
    }

    @Override
    public Employee findEmployeesByEmployeeId(Long employeeId) {
        return employeeRepository.findEmployeeByEmployeeId(employeeId);
    }


    @Override
    public Page<EmployeePaginationDto> getAllEmployeesByType(Pageable pageable, EmployeeType type) {
        return employeeRepository.getAllEmployeesByType(pageable, type);
    }


    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }


    @Override
    public Employee findEmployeeByEmail(String emailId) {

        return employeeRepository.findByEmail(emailId);
    }


    @Override
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }


    @Override
    public Employee findByEmployeeByEmployeeId(Long id) {
        return employeeRepository.findEmployeeByEmployeeId(id);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }



    @Override
    public List<Employee> findEmployeesByEmployeeType(EmployeeType employeeType) {
        return employeeRepository.findEmployeesByEmployeeType(employeeType);
    }


}
