package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.repository.RoundsRepository;
import com.divum.hiring_platform.repository.service.RoundsRepositoryService;
import com.divum.hiring_platform.util.enums.RoundType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundsRepositoryServiceImpl implements RoundsRepositoryService {


    private final RoundsRepository roundsRepository;

    @Override
    public Optional<Rounds> findById(String roundId) {
        return roundsRepository.findById(roundId);
    }

    @Override
    public void save(Rounds rounds) {
        roundsRepository.save(rounds);
    }

    @Override
    public List<Rounds> findByContestAndRoundType(Contest contestId, RoundType roundTypes) {
        return roundsRepository.findAllByContestAndRoundType(contestId, roundTypes);
    }

    public List<Rounds> findRoundsByContest(Contest contest) {
        return roundsRepository.findRoundsByContest(contest);
    }

    @Override
    public Rounds findByContestAndRoundNumber(Contest contest, int roundsNum) {
        return roundsRepository.findByContestAndRoundNumber(contest, roundsNum);
    }


    @Override
    public List<Rounds> findByContestAndInRoundType(Contest contest, List<RoundType> technicalInterview) {
        return roundsRepository.findByContestAndRoundTypeIn(contest,technicalInterview);
    }
}
