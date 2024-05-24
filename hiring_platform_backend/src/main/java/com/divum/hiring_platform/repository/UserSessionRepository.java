package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,String> {
    List<UserSession> findAllByRoundIdAndEmail(String id, String email);
    UserSession findByUniqueId(String uniqueId);

    UserSession findByUniqueIdAndLogoutTimeIsNull(String jit);
}
