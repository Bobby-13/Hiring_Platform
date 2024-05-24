package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.RoundAndCodingQuestion;

import java.util.List;

public interface RoundsAndQuestionRepositoryService {

    List<CodingQuestion> getQuestionByContest(Contest contest);

    int getQuestionCount(Contest contest);

    void saveAll(List<RoundAndCodingQuestion> roundAndMcqQuestions);

    void save(RoundAndCodingQuestion codingQuestion);

    void deleteWithContestId(String contestId);

    boolean isAssigned(String contestId);
}
