package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.MCQResult;

import java.util.List;
import java.util.Optional;

public interface McqResultUserRepositoryService {
    Optional<MCQResult> findById(String resultId);
    public List<MCQResult> findAll();

}
