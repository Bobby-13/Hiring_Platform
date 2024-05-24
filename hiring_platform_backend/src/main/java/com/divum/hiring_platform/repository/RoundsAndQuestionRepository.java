package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.ContestAndCoding;
import com.divum.hiring_platform.entity.RoundAndCodingQuestion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundsAndQuestionRepository extends JpaRepository<RoundAndCodingQuestion, ContestAndCoding> {


    @Query("SELECT COUNT(r) FROM RoundAndCodingQuestion r WHERE r.contestAndCoding.contest =?1")
    int getQuestionCount(Contest contest);

    @Query("SELECT coding.contestAndCoding.codingQuestion FROM RoundAndCodingQuestion coding WHERE coding.contestAndCoding.contest =?1")
    List<CodingQuestion> getQuestionByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM RoundAndCodingQuestion entry WHERE entry.contestAndCoding.contest.contestId =?1")
    void deleteWithContestId(String contestId);

    @Query("SELECT COUNT(r) > 0 FROM RoundAndCodingQuestion  r WHERE r.contestAndCoding.contest.contestId =?1")
    boolean isAssigned(String contestId);

    @Query(value = "SELECT q.contestAndCoding.codingQuestion FROM RoundAndCodingQuestion q WHERE q.rounds.id =?1")
    List<CodingQuestion> findByRoundId(String id);
}
