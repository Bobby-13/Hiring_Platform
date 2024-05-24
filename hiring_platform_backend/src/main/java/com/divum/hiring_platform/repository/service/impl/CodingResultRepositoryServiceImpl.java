package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.CodingResult;
import com.divum.hiring_platform.repository.CodingResultRepository;
import com.divum.hiring_platform.repository.service.CodingResultRepositoryService;
import com.divum.hiring_platform.util.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodingResultRepositoryServiceImpl implements CodingResultRepositoryService {
    private final CodingResultRepository codingResultRepository;
    @Override
    public int countByRoundId(String roundId) {
        return codingResultRepository.countByRoundId(roundId);
    }

    @Override
    public List<CodingResult> findCodingResultsByRoundId(String id) {
        return codingResultRepository.findCodingResultsByRoundId(id);
    }

    @Override
    public String findCodingResultsByRoundIdAndUserId(String previousRoundId, String userId) {
        return codingResultRepository.findCodingResultByRoundIdAndUserId(previousRoundId, userId).getId();
    }

    @Override
    public void saveAll(List<CodingResult> results) {
        codingResultRepository.saveAll(results);
    }

    @Override
    public List<CodingResult> findCodingRoundPassedContestants(String roundId, Result result) {
        return codingResultRepository.findCodingResultByRoundIdAndResult(roundId, result);
    }

    @Override
    public CodingResult findByRoundIdAndUserId(String id, String userId) {
        return codingResultRepository.findByRoundIdAndUserId(id, userId);
    }

    public List<CodingResult> findCodingResultsByUserIdAndContestId(String userId, String contestId) {
        return codingResultRepository.findCodingResultsByUserIdAndContestId(userId,contestId);
    }




}
