package com.divum.hiring_platform.repository.service;


import com.divum.hiring_platform.entity.UserSession;

import java.util.List;

public interface UserSessionRepositoryService {
    UserSession findByUniqueId(String jit);

    void save(UserSession userSession);

    List<UserSession> findAllByRoundIdAndEmail(String id, String email);
}
