package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.EmployeeDto;
import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.util.enums.EmployeeType;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {
    ResponseEntity<ResponseDto> addEmployee(Employee employee);

    ResponseEntity<ResponseDto> getEmployee(Long id);

    ResponseEntity<ResponseDto> updateEmployee(Long id, EmployeeDto employeeDto);

    ResponseEntity<ResponseDto> deleteEmployee(Long id);

    ResponseEntity<ResponseDto> getAllEmployees(Pageable pageable, EmployeeType type);

    ResponseEntity<ResponseDto> setResponse(String token) throws MessagingException;

    ResponseEntity<ResponseDto> changePassword(String emailId, String method, Password password);

    ResponseEntity<ResponseDto> employeePasswordReset(String token, Password password);
}
