package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MCQResult;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.repository.MCQResultRepository;
import com.divum.hiring_platform.repository.service.MCQResultRepositoryService;
import com.divum.hiring_platform.util.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MCQResultRepositoryServiceImpl implements MCQResultRepositoryService {
    private final MCQResultRepository mcqResultRepository;


    @Override
    public Optional<MCQResult> findByUserIdAndRoundId(String userId, String roundId) {
        return mcqResultRepository.findByUserIdAndRoundId(userId, roundId);

    }

    @Override
    public int countByRoundId(String roundId) {
        return mcqResultRepository.countByRoundId(roundId);
    }

    @Override
    public List<MCQResult> findMCQResultsByRoundId(String id) {
        return mcqResultRepository.findMCQResultsByRoundId(id);
    }

    @Override
    public String getMcqResultId(String previousRoundId, String userId) {
        MCQResult result = mcqResultRepository.findMCQResultsByRoundIdAndUserId(previousRoundId, userId);
        return result != null ? result.getId() : null;
    }

    @Override
    public void saveAll(List<MCQResult> mcqResults) {
        mcqResultRepository.saveAll(mcqResults);
    }

    @Override
    public List<MCQResult> findMCQResultsByRoundIdAndResult(String roundId, Result result) {
        return mcqResultRepository.findMCQResultsByRoundIdAndResult(roundId, result);
    }

    @Override
    public List<MCQResult> findMCQResultsByRoundIdAndResultIsPass(String id) {
        return mcqResultRepository.findMCQResultsByRoundIdAndResultIsPass(id);
    }

    @Override
    public List<MCQResult> findMcqResultsByUserAndContest(User user, Contest contest) {
        return mcqResultRepository.findMcqResultsByUserIdAndContestId(user.getUserId(),contest.getContestId());
    }
}
