package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.util.enums.RoundType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundsRepository extends JpaRepository<Rounds, String> {

    List<Rounds> findAllByContestAndRoundType(Contest contest, RoundType roundType);

    List<Rounds> findRoundsByContest(Contest contest);
    Rounds findByContestAndRoundNumber(Contest contest, int roundNum);

    @Query("SELECT r FROM Contest c JOIN c.rounds r WHERE c = ?1 AND r.roundType IN ?2")
    List<Rounds> findContestsByContestAndInRoundType(Contest contest, List<RoundType> roundTypes);

    List<Rounds> findByContestAndRoundTypeIn(Contest contest, List<RoundType> roundTypes);
}
