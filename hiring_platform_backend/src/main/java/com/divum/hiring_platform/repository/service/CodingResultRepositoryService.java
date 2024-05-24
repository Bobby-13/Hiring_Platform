package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.CodingResult;
import com.divum.hiring_platform.util.enums.Result;

import java.util.List;


public interface CodingResultRepositoryService {
    int countByRoundId(String roundId);

    List<CodingResult> findCodingResultsByRoundId(String id);

    String findCodingResultsByRoundIdAndUserId(String previousRoundId, String userId);

    void saveAll(List<CodingResult> results);

    List<CodingResult> findCodingResultsByUserIdAndContestId(String userId, String contestId);

    List<CodingResult> findCodingRoundPassedContestants(String roundId, Result result);

    CodingResult findByRoundIdAndUserId(String id, String userId);
}
