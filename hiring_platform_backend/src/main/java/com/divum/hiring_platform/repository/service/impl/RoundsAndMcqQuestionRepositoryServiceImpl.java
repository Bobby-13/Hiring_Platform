package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.entity.RoundAndMcqQuestion;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.repository.RoundAndMcqQuestionRepository;
import com.divum.hiring_platform.repository.service.RoundsAndMcqQuestionRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundsAndMcqQuestionRepositoryServiceImpl implements RoundsAndMcqQuestionRepositoryService {
    private final RoundAndMcqQuestionRepository roundsAndMcqQuestionRepository;


    @Override
    public List<MultipleChoiceQuestion> getQuestionByContest(Contest contest) {
        return roundsAndMcqQuestionRepository.getQuestionByContest(contest);
    }

    @Override
    public void save(RoundAndMcqQuestion roundAndMcqQuestion) {
        roundsAndMcqQuestionRepository.save(roundAndMcqQuestion);
    }

    @Override
    public void deleteWithContestId(String contestId) {
        roundsAndMcqQuestionRepository.deleteWithContestId(contestId);
    }

    @Override
    public int getQuestionCount(Contest contest) {
        return roundsAndMcqQuestionRepository.getQuestionCount(contest);
    }

    @Override
    public boolean isAssigned(String contestId) {
        return roundsAndMcqQuestionRepository.isAssigned(contestId);
    }

    public List<RoundAndMcqQuestion> findRoundAndMcqQuestionByMcq(MultipleChoiceQuestion multipleChoiceQuestion) {
        return roundsAndMcqQuestionRepository.findRoundAndMcqQuestionByMcq(multipleChoiceQuestion);
    }

    @Override
    public void delete(RoundAndMcqQuestion roundAndMcqQuestion) {
        roundsAndMcqQuestionRepository.delete(roundAndMcqQuestion);
    }

    public List<MultipleChoiceQuestion> findRoundAndMcqQuestionByRoundsId(Rounds rounds) {
        return roundsAndMcqQuestionRepository.findRoundAndMcqQuestionByRoundsId(rounds);
    }

    @Override
    public List<MultipleChoiceQuestion> findByRoundId(String roundId) {
        return roundsAndMcqQuestionRepository.findByRoundId(roundId);
    }
}
