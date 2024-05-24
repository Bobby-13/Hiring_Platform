package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.repository.UserRepository;
import com.divum.hiring_platform.repository.service.UserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;

    @Override
    public Long countUsersByContest(Contest contest) {
        return userRepository.countUsersByContest(contest);
    }

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public Set<Contest> getParticipatedContest(String userId) {
        return userRepository.getParticipatedContest(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String username) {
        return userRepository.findUserByEmail(username);
    }

    @Override
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
