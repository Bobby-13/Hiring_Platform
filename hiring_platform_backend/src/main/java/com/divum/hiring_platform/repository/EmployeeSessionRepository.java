package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.EmployeeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSessionRepository extends JpaRepository<EmployeeSession, String> {
    EmployeeSession findByUniqueIdAndLogoutTimeNull(String jit);
}
