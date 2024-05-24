package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.MCQResult;
import com.divum.hiring_platform.repository.McqResultUserRepository;
import com.divum.hiring_platform.repository.service.McqResultUserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class McqResultUserRepositoryServiceImpl implements McqResultUserRepositoryService {

    private final McqResultUserRepository mcqResultUserRepository;

    @Override
    public Optional<MCQResult> findById(String resultId) {
        return mcqResultUserRepository.findById(resultId);
    }

    @Override
    public List<MCQResult> findAll() {
        return mcqResultUserRepository.findAll();
    }

}
