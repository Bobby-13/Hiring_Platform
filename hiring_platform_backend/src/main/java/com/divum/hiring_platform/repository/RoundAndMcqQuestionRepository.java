package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.entity.RoundAndMcqQuestion;
import com.divum.hiring_platform.entity.Rounds;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundAndMcqQuestionRepository extends JpaRepository<RoundAndMcqQuestion, Long> {


    @Query("SELECT COUNT(r) > 0 FROM RoundAndMcqQuestion r WHERE r.contestAndMcq.contest =?1 AND r.contestAndMcq.multipleChoiceQuestion =?2")
    boolean checkIfTheQuestionIsAssigned(Contest contest, MultipleChoiceQuestion question);

    @Query("SELECT COUNT(*) FROM RoundAndMcqQuestion WHERE contestAndMcq.contest =?1")
    int getQuestionCount(Contest contest);

    @Query("SELECT contestAndMcq.multipleChoiceQuestion FROM RoundAndMcqQuestion WHERE contestAndMcq.contest =?1")
    List<MultipleChoiceQuestion> getQuestionByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM RoundAndMcqQuestion WHERE contestAndMcq.contest.contestId =?1")
    void deleteWithContestId(String contestId);

    @Query("SELECT COUNT(*) > 0 FROM RoundAndMcqQuestion WHERE contestAndMcq.contest.contestId =?1")
    boolean isAssigned(String contestId);

    @Query(value = "SELECT q.contestAndMcq.multipleChoiceQuestion FROM RoundAndMcqQuestion q WHERE q.rounds.id =?1")
    List<MultipleChoiceQuestion> findByRoundId(String roundsId);

    @Query("SELECT raq FROM RoundAndMcqQuestion raq WHERE raq.contestAndMcq.multipleChoiceQuestion = :mcqQuestion")
    List<RoundAndMcqQuestion> findRoundAndMcqQuestionByMcq(MultipleChoiceQuestion mcqQuestion);

    @Query("SELECT assinedQuestion.contestAndMcq.multipleChoiceQuestion FROM RoundAndMcqQuestion assinedQuestion WHERE assinedQuestion.rounds =?1")
    List<MultipleChoiceQuestion> findRoundAndMcqQuestionByRoundsId(@Param("rounds") Rounds rounds);

}
