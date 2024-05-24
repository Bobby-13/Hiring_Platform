package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.RoundAndCodingQuestion;
import com.divum.hiring_platform.repository.RoundsAndQuestionRepository;
import com.divum.hiring_platform.repository.service.RoundsAndQuestionRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundAndQuestionRepositoryServiceImpl implements RoundsAndQuestionRepositoryService {
    private final RoundsAndQuestionRepository roundsAndQuestionRepository;


    @Override
    public List<CodingQuestion> getQuestionByContest(Contest contest) {
        return roundsAndQuestionRepository.getQuestionByContest(contest);
    }

    @Override
    public int getQuestionCount(Contest contest) {
        return roundsAndQuestionRepository.getQuestionCount(contest);
    }

    @Override
    public void saveAll(List<RoundAndCodingQuestion> roundAndMcqQuestions) {
        roundsAndQuestionRepository.saveAll(roundAndMcqQuestions);
    }

    @Override
    public void save(RoundAndCodingQuestion codingQuestion) {
        roundsAndQuestionRepository.save(codingQuestion);
    }

    @Override
    public void deleteWithContestId(String contestId) {
        roundsAndQuestionRepository.deleteWithContestId(contestId);
    }

    @Override
    public boolean isAssigned(String contestId) {
        return roundsAndQuestionRepository.isAssigned(contestId);
    }
}
