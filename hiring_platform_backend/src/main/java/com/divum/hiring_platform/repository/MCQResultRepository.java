package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.MCQResult;
import com.divum.hiring_platform.util.enums.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MCQResultRepository extends MongoRepository<MCQResult, String> {


    Optional<MCQResult> findByUserIdAndRoundId(String userId, String roundId);

    List<MCQResult> findMCQResultsByRoundId(String roundId);

    int countByRoundId(String roundId);

    MCQResult findByRoundIdAndUserId(String userId, String id);

    List<MCQResult> findMCQResultsByRoundIdAndResult(String roundId, Result result);

    MCQResult findMCQResultsByRoundIdAndUserId(String previousRoundId, String userId);

    MCQResult findMCQResultById(String id);

    @Query("{'roundId': ?0, 'result': 'PASS'}")
    List<MCQResult> findMCQResultsByRoundIdAndResultIsPass(String roundId);



    List<MCQResult> findMcqResultsByUserIdAndContestId(String userId, String contestId);
}
