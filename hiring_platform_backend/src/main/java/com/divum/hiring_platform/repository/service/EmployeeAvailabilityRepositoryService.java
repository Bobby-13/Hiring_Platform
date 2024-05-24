package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.EmployeeAndContest;
import com.divum.hiring_platform.entity.EmployeeAvailability;
import com.divum.hiring_platform.util.enums.EmployeeType;

import java.util.List;

public interface EmployeeAvailabilityRepositoryService {
    Employee findEmployeesWhoIsAvailable(Long employeeId, Contest contest, EmployeeType employeeType);

    void saveAll(List<EmployeeAvailability> employeeAvailabilities);

    List<EmployeeAvailability> findEmployeeAvailabilitiesByContest(String contestId);

    EmployeeAvailability findEmployeeAvailabilityByEmployeeAndContest(EmployeeAndContest employeeAndContest);

    void save(EmployeeAvailability employeeAvailability);

    void deleteRecord(String contestId);
}
