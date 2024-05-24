package com.divum.hiring_platform.service.impl;


import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.EmployeeAndContest;
import com.divum.hiring_platform.entity.EmployeeAvailability;
import com.divum.hiring_platform.exception.InvalidDataException;
import com.divum.hiring_platform.repository.service.EmployeeAvailabilityRepositoryService;
import com.divum.hiring_platform.repository.service.EmployeeRepositoryService;
import com.divum.hiring_platform.service.EmployeeService;
import com.divum.hiring_platform.util.ContestRelatedService;
import com.divum.hiring_platform.util.EmailSender;
import com.divum.hiring_platform.util.JwtUtil;
import com.divum.hiring_platform.util.enums.EmployeeResponse;
import com.divum.hiring_platform.util.enums.EmployeeType;
import com.divum.hiring_platform.util.enums.Role;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.divum.hiring_platform.strings.Strings.EMPLOYEE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final JwtUtil jwtUtil;
    private final EmployeeAvailabilityRepositoryService employeeAvailabilityRepositoryService;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepositoryService employeeRepositoryService;
    private final ContestRelatedService contestRelatedService;
    @Override
    public ResponseEntity<ResponseDto> addEmployee(Employee employee) {
        employee.setActive(true);
        Employee employee1;
        employee1 = employeeRepositoryService.findByEmail(employee.getEmail());
        if (employee1 != null) {
            throw new InvalidDataException("Duplicate resource found");
        }

        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setActive(true);

        employeeRepositoryService.save(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("employee is successfully added", employee));
    }

    @Override
    public ResponseEntity<ResponseDto> getEmployee(Long id) {

        Employee employee = employeeRepositoryService.findByEmployeeByEmployeeId(id);
        if (employee == null || !employee.isActive())
            throw new ResourceNotFoundException(EMPLOYEE_NOT_FOUND + " " + id);
        employee.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("here is the employee", employee));
    }

    @Override
    public ResponseEntity<ResponseDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepositoryService.findByEmployeeByEmployeeId(id);

        if (employee == null)
            throw new ResourceNotFoundException("Employee not found");
        updateEmployeeFields(employee, employeeDto);
        employeeRepositoryService.save(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("employee data is updated", null));

    }

    private void updateEmployeeFields(Employee employee, EmployeeDto employeeDto) {
        if (employeeDto.getEmail() != null) employee.setEmail(employeeDto.getEmail());
        if (employeeDto.getEmployeeType() != null) employee.setEmployeeType(employeeDto.getEmployeeType());
        if (employeeDto.getFirstName() != null) employee.setFirstName(employeeDto.getFirstName());
        if (employeeDto.getLastName() != null) employee.setLastName(employeeDto.getLastName());
        if (employeeDto.getRole() != null) employee.setRole(employeeDto.getRole());
        if (employeeDto.getPassword() != null) employee.setPassword(employeeDto.getPassword());
        if (employeeDto.getStack() != null) employee.setStack(employeeDto.getStack());
        if (employeeDto.getYearsOfExperience() != 0) employee.setYearsOfExperience(employeeDto.getYearsOfExperience());

    }

    @Override
    public ResponseEntity<ResponseDto> deleteEmployee(Long id) {
        Employee employee = employeeRepositoryService.findByEmployeeByEmployeeId(id);

        if (employee == null)
            throw new ResourceNotFoundException("Employee not found");

        employee.setActive(false);
        employeeRepositoryService.save(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("employee is deleted", null));


    }

    @Override
    public ResponseEntity<ResponseDto> getAllEmployees(Pageable pageable, EmployeeType type) {
        if (type == null) {
            Page<EmployeePaginationDto> employees = employeeRepositoryService.getAllEmployees(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("all the employees", employees));
        } else {
            Page<EmployeePaginationDto> employees = employeeRepositoryService.getAllEmployeesByType(pageable, type);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("all the employees by type", employees));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> setResponse(String token) throws MessagingException {
        EmployeeResponseDTO employeeResponse;
        try {
            employeeResponse = jwtUtil.extractEmployeeResponse(token);
            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Token expired contact your admin", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Not a valid token", null));
        }
        Employee employee = null;
        Contest contest = contestRelatedService.getContestFromDatabase(employeeResponse.getContestId());

        try {
            employee = employeeRepositoryService.findById(employeeResponse.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee with the id " + employeeResponse.getEmployeeId() + "not found"));
        } catch (Exception e) {
            resourceNotFound(e.getMessage());
        }
        EmployeeAvailability employeeAvailability = employeeAvailabilityRepositoryService.findEmployeeAvailabilityByEmployeeAndContest(new EmployeeAndContest(employee, contest));
        if (employeeAvailability.getResponse() != EmployeeResponse.PENDING) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("You have already responded", null));
        }
        if (employeeResponse.getDecision().equals("ACCEPT")) {
            employeeAvailability.setResponse(EmployeeResponse.AVAILABLE);
        } else {
            employeeAvailability.setResponse(EmployeeResponse.NOT_AVAILABLE);
        }
        employeeAvailabilityRepositoryService.save(employeeAvailability);
        emailSender.sendConfirmationMail(employeeAvailability);
        return ResponseEntity.ok(new ResponseDto("Email send to the employees", null));
    }

    @Override
    public ResponseEntity<ResponseDto> changePassword(String emailId, String method, Password password) {
        Employee employee = employeeRepositoryService.findEmployeeByEmail(emailId);
        if (employee == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(emailId + " Not found", null));
        }
        assert employee != null;
        if (method.equals("FORGET_PASSWORD")) {
            String token = jwtUtil.generateForgotPasswordToken(employee.getEmployeeId());
            try {
                emailSender.sendEmailToTheEmployeeToResetThePassword(employee, token);
                return ResponseEntity.ok(new ResponseDto("Email has been sent to the user with password reset link", null));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(e.getMessage(), null));
            }
        } else if (method.equals("CHANGE_PASSWORD")) {
            String oldPassword = password.getOldPassword();
            if (passwordEncoder.matches(oldPassword, employee.getPassword())) {
                employee.setPassword(passwordEncoder.encode(password.getNewPassword()));
                employeeRepositoryService.save(employee);
                try {
                    emailSender.sendEmailToTheEmployeeAboutPasswordChange(employee);
                } catch (MessagingException e) {
                    return ResponseEntity.ok(new ResponseDto("Password has been changed", e.getMessage()));
                }
                return ResponseEntity.ok(new ResponseDto("Password has been changed", null));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("You password does not match", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Not a valid request", null));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> employeePasswordReset(String token, Password password) {
        Long id;
        try {
            id = Long.valueOf(jwtUtil.extractEmployeeId(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Not a valid token", null));
        }
        Employee employee = employeeRepositoryService.findEmployeeByEmployeeId(id);
        employee.setPassword(passwordEncoder.encode(password.getNewPassword()));
        employeeRepositoryService.save(employee);
        try {
            emailSender.sendEmailToTheEmployeeAboutPasswordChange(employee);
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto("Password has been updated", e.getMessage()));
        }
        return ResponseEntity.ok(new ResponseDto("Password has been updated", null));
    }

    public void resourceNotFound(String message) {
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(message, null));
    }


}
