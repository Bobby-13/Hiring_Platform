package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.entity.RoundAndMcqQuestion;
import com.divum.hiring_platform.entity.Rounds;

import java.util.List;

public interface RoundsAndMcqQuestionRepositoryService {

    List<MultipleChoiceQuestion> getQuestionByContest(Contest contest);

    void save(RoundAndMcqQuestion roundAndMcqQuestion);

    void deleteWithContestId(String contestId);

    int getQuestionCount(Contest contest);

    boolean isAssigned(String contestId);

    List<RoundAndMcqQuestion> findRoundAndMcqQuestionByMcq(MultipleChoiceQuestion multipleChoiceQuestion);

    void delete(RoundAndMcqQuestion roundAndMcqQuestion);

    List<MultipleChoiceQuestion> findRoundAndMcqQuestionByRoundsId(Rounds rounds );

    List<MultipleChoiceQuestion> findByRoundId(String roundId);
}
