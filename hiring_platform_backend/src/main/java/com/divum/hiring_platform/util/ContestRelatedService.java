package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.Contestants;
import com.divum.hiring_platform.dto.RoundFilterContestantDTO;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.CodingResultRepositoryService;
import com.divum.hiring_platform.repository.service.ContestRepositoryService;
import com.divum.hiring_platform.repository.service.MCQResultRepositoryService;
import com.divum.hiring_platform.repository.service.UserRepositoryService;
import com.divum.hiring_platform.util.enums.ContestStatus;
import com.divum.hiring_platform.util.enums.Result;
import com.divum.hiring_platform.util.enums.RoundType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.divum.hiring_platform.strings.Strings.*;

@Service
@RequiredArgsConstructor
public class ContestRelatedService {

    private final ContestRepositoryService contestRepositoryService;
    private final UserRepositoryService userRepositoryService;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final EmailSender emailSender;
    private final CodingResultRepositoryService codingResultRepositoryService;

    @NotNull
    public Contest getContestFromDatabase(String contestId) {
        Optional<Contest> optionalContest = contestRepositoryService.findById(contestId);
        if (optionalContest.isEmpty()) {
            throw new ResourceNotFoundException(CONTEST_NOT_FOUND + " " + contestId);
        }
        return optionalContest.get();
    }

    public Rounds getPreviousRound(Contest contest, Rounds currentRound) {
        for (Rounds round : contest.getRounds()) {
            if (currentRound.getRoundNumber() == round.getRoundNumber() + 1) {
                return round;
            }
        }
        return null;
    }


    public String getInterviewType(RoundType roundType) {
        return roundType == RoundType.PERSONAL_INTERVIEW ? PERSONAL : TECHNICAL;
    }

    @NotNull
    public RoundFilterContestantDTO getUpdatedMcqResult(String roundId, Rounds round) {
        RoundFilterContestantDTO filterContestantDTO = new RoundFilterContestantDTO();
        filterContestantDTO.setContestName(round.getContest().getName());
        filterContestantDTO.setRoundType(String.valueOf(round.getRoundType()));
        filterContestantDTO.setRoundNumber(round.getRoundNumber());
        emailSender.updateResult(round, new ArrayList<>());
        List<MCQResult> mcqResults = mcqResultRepositoryService.findMCQResultsByRoundIdAndResult(roundId, Result.PASS);
        List<Contestants> contestants = updatedResultInfo(mcqResults);
        filterContestantDTO.setContestantLists(contestants);
        return filterContestantDTO;
    }

    public <t extends ResultEntity> List<Contestants> updatedResultInfo(@NotNull List<t> results) {
        List<Contestants> contestants = new ArrayList<>();
        for (t result : results) {
            User user = getUserFromDatabase(result.getUserId());
            buildContestants(result.getTotalPercentage(), user, contestants);
            updateContestantStatus(result.getResult(), user);
        }
        saveResults(results);
        return contestants;
    }
    private <T extends ResultEntity> void saveResults(List<T> results) {
        if (!results.isEmpty()) {
            if (results.get(0) instanceof CodingResult) {
                List<CodingResult> codingResults = results.stream()
                        .filter(r -> r instanceof CodingResult)
                        .map(r -> (CodingResult) r)
                        .collect(Collectors.toList());
                codingResultRepositoryService.saveAll(codingResults);
            } else if (results.get(0) instanceof MCQResult) {
                List<MCQResult> mcqResults = results.stream()
                        .filter(r -> r instanceof MCQResult)
                        .map(r -> (MCQResult) r)
                        .collect(Collectors.toList());
                mcqResultRepositoryService.saveAll(mcqResults);
            }
        }
    }

    public List<Contestants> updatedCodingResult(@NotNull List<CodingResult> mcqResults, int passPercentage) {
        List<Contestants> contestants = new ArrayList<>();
        for (CodingResult result : mcqResults) {
            User user = getUserFromDatabase(result.getUserId());
            result.setResult(result.getTotalPercentage() >= passPercentage ? Result.PASS : Result.FAIL);
            updateContestantStatus(result.getResult(), user);
            if(result.getResult().equals(Result.FAIL)) continue;
            buildContestants(result.getTotalPercentage(), user, contestants);
        }
        codingResultRepositoryService.saveAll(mcqResults);
        return contestants;
    }

    public boolean isExistingUser(String email) {
        return userRepositoryService.existsUserByEmail(email);
    }

    public void assignUserToTheContest(String email, Contest contest, List<String> errorList) {
        User user = userRepositoryService.findByEmail(email);
        Set<Contest> contestList = userRepositoryService.getParticipatedContest(user.getUserId());
        for (Contest enrolledContest : contestList) {
            if (enrolledContest.getContestStatus() == ContestStatus.CURRENT || enrolledContest.getContestStatus() == ContestStatus.UPCOMING) {
                errorList.add(user.getEmail());
                return;
            }
        }
        contestList.add(contest);
        user.setContest(contestList);
        userRepositoryService.save(user);
    }

    public void buildContestants(Float score, User user, List<Contestants> contestants) {
        Contestants contestant = new Contestants();
        contestant.setEmail(user.getEmail());
        if (score != null) {
            contestant.setScore(score);
        }
        contestant.setName(user.getName());
        contestant.setCollegeName(user.getCollegeName());
        contestants.add(contestant);
    }
    @NotNull
    public User getUserFromDatabase(String userId) {
        Optional<User> optionalUser = userRepositoryService.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return optionalUser.get();
    }

    public RoundFilterContestantDTO getUpdatedCodingResult(String roundId, Rounds round) {
        RoundFilterContestantDTO filterContestantDTO = new RoundFilterContestantDTO();
        filterContestantDTO.setContestName(round.getContest().getName());
        filterContestantDTO.setRoundType(String.valueOf(round.getRoundType()));
        filterContestantDTO.setRoundNumber(round.getRoundNumber());
        emailSender.updateResult(round, new ArrayList<>());
        List<CodingResult> mcqResults = codingResultRepositoryService.findCodingRoundPassedContestants(roundId, Result.PASS);
        List<Contestants> contestants = updatedResultInfo(mcqResults);
        filterContestantDTO.setContestantLists(contestants);
        return filterContestantDTO;
    }
    private void updateContestantStatus(Result result, User user) {
        if (result.equals(Result.PASS)) {
            user.setPassed(true);
        } else if (result.equals(Result.FAIL)) {
            user.setPassed(false);
        }
        userRepositoryService.save(user);
    }
}
