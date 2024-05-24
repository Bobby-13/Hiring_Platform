package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.InterviewRequest;

import java.util.List;
import java.util.Optional;

public interface InterviewRequestRepositoryService {
    List<InterviewRequest> findCurrentInterviewRequest(String id);

    Optional<InterviewRequest> findById(Long requestId);


    void save(InterviewRequest interviewRequest);
}
