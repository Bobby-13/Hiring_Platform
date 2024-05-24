package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.UserSession;
import com.divum.hiring_platform.repository.UserSessionRepository;
import com.divum.hiring_platform.repository.service.UserSessionRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserSessionRepositoryServiceImpl implements UserSessionRepositoryService {

    private final UserSessionRepository userSessionRepository;

    @Override
    public UserSession findByUniqueId(String jit) {
        return userSessionRepository.findByUniqueIdAndLogoutTimeIsNull(jit);
    }

    @Override
    public void save(UserSession userSession) {
        userSessionRepository.save(userSession);
    }

    @Override
    public List<UserSession> findAllByRoundIdAndEmail(String id, String email) {
        return userSessionRepository.findAllByRoundIdAndEmail(id,email);
    }
}
