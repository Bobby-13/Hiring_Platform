package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.UserFinalResultDto;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinalResultCalculationService {
    private final ContestRepositoryService contestRepositoryService;
    private final RoundsRepositoryService roundsRepositoryService;
    private final InterviewRepositoryService interviewRepositoryService;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    private final WriteExcelService writeExcel;
    private final ContestRelatedService contestRelatedService;

    public ByteArrayInputStream getFinalResult(String contestId) throws IOException {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        List<Rounds> roundsList = roundsRepositoryService.findRoundsByContest(contest);
        Rounds round = new Rounds();
        for (Rounds rounds : roundsList) {
            if (rounds.getRoundNumber() == roundsList.size()) {
                round = rounds;
            }
        }
        switch (round.getRoundType()) {
            case MCQ -> {
                List<UserFinalResultDto> userFinalResultDtoS = getFinalResultMCQ(roundsList, round.getId());
                return writeExcel.writeExcel(userFinalResultDtoS);
            }
            case CODING -> {
                List<UserFinalResultDto> userFinalResultDtoS = getFinalResultCoding(roundsList, round.getId());
                return writeExcel.writeExcel(userFinalResultDtoS);
            }
            case PERSONAL_INTERVIEW, TECHNICAL_INTERVIEW -> {
                List<UserFinalResultDto> userFinalResultDtoS = getFinalResultInterview(roundsList, round.getId());
                return writeExcel.writeExcel(userFinalResultDtoS);
            }
        }
        return null;
    }

    private List<UserFinalResultDto> getFinalResultInterview(List<Rounds> roundLists, String id) {
        List<Interview> interview = interviewRepositoryService.findInterviewsByRoundsId(id);
        List<User> users = new ArrayList<>();
        for (Interview interview1 : interview) {
            users.add(interview1.getUser());
        }
        return userFinalResultDtoS(roundLists, users, id);
    }

    private List<UserFinalResultDto> getFinalResultCoding(List<Rounds> roundList, String id) {
        List<CodingResult> codingResults = codingResultRepositoryService.findCodingResultsByRoundId(id);
        List<User> users = new ArrayList<>();
        for (CodingResult codingResult : codingResults) {
            User user = contestRelatedService.getUserFromDatabase(codingResult.getUserId());
            users.add(user);
        }
        return userFinalResultDtoS(roundList, users, id);
    }

    private List<UserFinalResultDto> getFinalResultMCQ(List<Rounds> roundList, String id) {
        List<MCQResult> mcqResults = mcqResultRepositoryService.findMCQResultsByRoundIdAndResultIsPass(id);
        List<User> users = new ArrayList<>();
        for (MCQResult mcqResult : mcqResults) {
            User user = contestRelatedService.getUserFromDatabase(mcqResult.getUserId());
            users.add(user);
        }
        return userFinalResultDtoS(roundList, users, id);
    }

    public List<UserFinalResultDto> userFinalResultDtoS(List<Rounds> roundsList, List<User> users, String id) {
        List<UserFinalResultDto> userFinalResultDtoList = new ArrayList<>();
        for (User user : users) {
            UserFinalResultDto userFinalResultDto = new UserFinalResultDto();
            userFinalResultDto.setUserName(user.getName());
            userFinalResultDto.setEmail(user.getEmail());
            Map<String, String> mcqResults = new HashMap<>();
            Contest contest = contestRepositoryService.findContestByRounds(roundsList.get(0));
            int i = 1;
            for (MCQResult mcqResult : mcqResultRepositoryService.findMcqResultsByUserAndContest(user, contest)) {
                mcqResults.put("MCQ-" + i++, String.valueOf(mcqResult.getTotalPercentage()));
            }
            userFinalResultDto.setMcqMark(mcqResults);
            i = 1;
            Map<String, String> codingResults = new HashMap<>();
            for (CodingResult codingResult : codingResultRepositoryService.findCodingResultsByUserIdAndContestId(user.getUserId(), contest.getContestId())) {
                codingResults.put("CODING-" + i++, String.valueOf(codingResult.getTotalPercentage()));
            }
            userFinalResultDto.setCodingMark(codingResults);
            userFinalResultDtoList.add(userFinalResultDto);


        }
        return userFinalResultDtoList;
    }
}
