package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.util.enums.RoundType;

import java.util.List;
import java.util.Optional;

public interface RoundsRepositoryService {

    List<Rounds> findByContestAndRoundType(Contest contestId, RoundType roundTypes);

    void save(Rounds previousRound);

    Optional<Rounds> findById(String roundId);

    List<Rounds> findRoundsByContest(Contest contest);

    Rounds findByContestAndRoundNumber(Contest contest, int roundsNum);

    List<Rounds> findByContestAndInRoundType(Contest contest, List<RoundType> technicalInterview);
}
