package com.divum.hiring_platform.repository.service;


import com.divum.hiring_platform.entity.EmployeeSession;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeSessionRepositoryService {

    EmployeeSession findByUniqueId(String jit);

    void save(EmployeeSession employeeSession);
}
