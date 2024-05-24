package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.InterviewRequest;
import com.divum.hiring_platform.repository.InterviewRequestRepository;
import com.divum.hiring_platform.repository.service.InterviewRequestRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewRequestRepositoryServiceImpl implements InterviewRequestRepositoryService {

    private final InterviewRequestRepository interviewRequestRepository;

    @Override
    public List<InterviewRequest> findCurrentInterviewRequest(String id) {
        return interviewRequestRepository.findCurrentInterviewRequest(id);
    }

    @Override
    public Optional<InterviewRequest> findById(Long requestId) {
        return interviewRequestRepository.findById(requestId);
    }

    @Override
    public void save(InterviewRequest interviewRequest) {
        interviewRequestRepository.save(interviewRequest);
    }
}
