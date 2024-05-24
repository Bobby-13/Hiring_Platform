package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.EmployeeSession;
import com.divum.hiring_platform.repository.EmployeeSessionRepository;
import com.divum.hiring_platform.repository.service.EmployeeSessionRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeSessionRepositoryServiceImpl implements EmployeeSessionRepositoryService {

    private final EmployeeSessionRepository employeeSessionRepository;

    @Override
    public EmployeeSession findByUniqueId(String jit) {
        return employeeSessionRepository.findByUniqueIdAndLogoutTimeNull(jit);
    }

    @Override
    public void save(EmployeeSession employeeSession) {
        employeeSessionRepository.save(employeeSession);
    }
}
