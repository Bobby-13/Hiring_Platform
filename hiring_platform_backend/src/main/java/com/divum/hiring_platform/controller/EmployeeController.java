package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.EmployeeApi;
import com.divum.hiring_platform.dto.EmployeeDto;
import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.service.EmployeeService;
import com.divum.hiring_platform.util.enums.EmployeeType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;


    @Override
    public ResponseEntity<ResponseDto> addEmployee(Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @Override
    public ResponseEntity<ResponseDto> getEmployee(Long id) {
        return employeeService.getEmployee(id);
    }

    @Override
    public ResponseEntity<ResponseDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        return employeeService.updateEmployee(id, employeeDto);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteEmployee(Long id) {
        return employeeService.deleteEmployee(id);
    }

    @Override
    public ResponseEntity<ResponseDto> getAllEmployees(Pageable pageable, EmployeeType type) {
        return employeeService.getAllEmployees(pageable, type);
    }

    @Override
    public ResponseEntity<ResponseDto> setResponse(String token) throws MessagingException {
        return employeeService.setResponse(token);
    }

    @Override
    public ResponseEntity<ResponseDto> changePassword(String emailId, String method, Password password) {
        return employeeService.changePassword(emailId, method, password);
    }

    @Override
    public ResponseEntity<ResponseDto> employeeResetPassword(String token, Password password) {
        return employeeService.employeePasswordReset(token, password);
    }

}
