package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepositoryService {

    Long countUsersByContest(Contest contest);

    void saveAll(List<User> users);

    boolean existsUserByEmail(String email);

    Set<Contest> getParticipatedContest(String userId);

    List<User> findAll();

    User findByEmail(String username);

    void save(User user);

    Optional<User> findById(String userId);

    User findUserByUserId(String userId);

    User findUserByEmail(String email);
}
