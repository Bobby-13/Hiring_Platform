package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.EmployeeDto;
import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.util.enums.EmployeeType;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/employee")
public interface EmployeeApi {
    @PostMapping
    ResponseEntity<ResponseDto> addEmployee(@RequestBody Employee employee);

    @GetMapping("/{id}")
    ResponseEntity<ResponseDto> getEmployee(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<ResponseDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto);

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseDto> deleteEmployee(@PathVariable Long id);

    @GetMapping
    ResponseEntity<ResponseDto> getAllEmployees(Pageable pageable, @RequestParam(required = false) EmployeeType type);

    @PostMapping("/response")
    ResponseEntity<ResponseDto> setResponse(@RequestParam(value = "token") String token) throws MessagingException;

    @PostMapping("/password-reset/email/{emailId}")
    ResponseEntity<ResponseDto> changePassword(@PathVariable String emailId, @RequestParam(value = "method") String method, @RequestBody(required = false) Password password);

    @PostMapping("/password-reset")
    ResponseEntity<ResponseDto> employeeResetPassword(@RequestParam(value = "token") String token, @RequestBody Password password);
}
