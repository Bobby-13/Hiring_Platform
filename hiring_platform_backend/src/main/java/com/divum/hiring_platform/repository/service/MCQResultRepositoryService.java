package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MCQResult;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.util.enums.Result;

import java.util.List;
import java.util.Optional;

public interface MCQResultRepositoryService {

    int countByRoundId(String roundId);

    List<MCQResult> findMCQResultsByRoundId(String id);

    String getMcqResultId(String previousRoundId, String userId);

    void saveAll(List<MCQResult> mcqResults);

    List<MCQResult> findMCQResultsByRoundIdAndResult(String roundId, Result result);
    Optional<MCQResult> findByUserIdAndRoundId(String userId, String roundId);

    List<MCQResult> findMCQResultsByRoundIdAndResultIsPass(String id);

    List<MCQResult> findMcqResultsByUserAndContest(User user, Contest contest);
}
