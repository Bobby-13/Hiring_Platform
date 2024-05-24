package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.CodingResultRepositoryService;
import com.divum.hiring_platform.repository.service.MCQResultRepositoryService;
import com.divum.hiring_platform.repository.service.UserRepositoryService;
import com.divum.hiring_platform.service.UserService;
import com.divum.hiring_platform.util.ContestRelatedService;
import com.divum.hiring_platform.util.enums.ContestStatus;
import com.divum.hiring_platform.util.enums.RoundType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryService userRepositoryService;
    private final ContestRelatedService contestRelatedService;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    @Override
    public ResponseEntity<ResponseDto> landingPageCredentials(String userId) {
        Set<Contest> contestAssignedToUser = userRepositoryService.getParticipatedContest(userId);
        List<ContestDetailsDto> contestDetailsDtoList = new ArrayList<>();
        for(Contest contest:contestAssignedToUser) {
            if (contest.getContestStatus() == ContestStatus.CURRENT) {
                List<RoundDetailsDto> roundDetailsList = new ArrayList<>();
                contest.getRounds().sort(Comparator.comparing(Rounds::getRoundNumber));
                for (Rounds rounds : contest.getRounds()) {
                    String status = "LOCKED";
                    if (rounds.getStartTime() != null && rounds.getStartTime().isBefore(LocalDateTime.now())) {
                        status = "ACTIVE";
                    }
                    status = updateStatus(status, userId, rounds);
                    RoundDetailsDto roundDetailsDto = RoundDetailsDto.builder()
                            .roundId(rounds.getId())
                            .roundName(rounds.getRoundType())
                            .startTime(rounds.getStartTime())
                            .endTime(rounds.getEndTime())
                            .status(status)
                            .build();
                    roundDetailsList.add(roundDetailsDto);
                }
                ContestDetailsDto contestDetailsDto = ContestDetailsDto.builder()
                        .contestId(contest.getContestId())
                        .contestName(contest.getName())
                        .contestStatus(contest.getContestStatus())
                        .round(roundDetailsList)
                        .build();

                contestDetailsDtoList.add(contestDetailsDto);
            }
        }
        if(contestDetailsDtoList.isEmpty()){
            return ResponseEntity.ok(new ResponseDto("No Contest Assigned ",null));
        }
        return ResponseEntity.ok(new ResponseDto("Contest Details For Individuals",contestDetailsDtoList));
    }

    private String updateStatus(String status, String userId, Rounds rounds) {
        if (rounds.getRoundType().equals(RoundType.MCQ)) {
            Optional<MCQResult> results = mcqResultRepositoryService.findByUserIdAndRoundId(userId, rounds.getId());
            if (results.isEmpty()) {
                return status;
            }
            MCQResult result = results.get();
            if (result.getTotalPercentage() != -1) {
                return "COMPLETED";
            }
        } else if (rounds.getRoundType().equals(RoundType.CODING)) {
            CodingResult codingResults = codingResultRepositoryService.findByRoundIdAndUserId(rounds.getId(), userId);
            if (codingResults == null) {
                return status;
            }
            if (codingResults.getTotalPercentage() != -1) {
                return "COMPLETED";
            }
        }
        return status;
    }


    @Override
    public ResponseEntity<ResponseDto> passwordReset(String email, Password passwordResetRequestDto) {
      User user = userRepositoryService.findByEmail(email);
      if(passwordEncoder.matches(passwordResetRequestDto.getOldPassword(), user.getPassword())){
          user.setPassword(passwordEncoder.encode(passwordResetRequestDto.getNewPassword()));
          userRepositoryService.save(user);
          return ResponseEntity.ok(new ResponseDto("Password Updated Successfully", null));
      }
      else{
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto("Current Password Mismatch", null));
      }
    }

    @Override
    public ResponseEntity<ResponseDto> resumeUpload(String userId,ResumeUploadRequestDto resumeUploadRequestDto) {

        User user = contestRelatedService.getUserFromDatabase(userId);

        Resume resume;
        if(user.getResume() != null) {
            resume = user.getResume();
        } else {
            resume = new Resume();
        }
        resume.setResumeUrl(resumeUploadRequestDto.getResumeUrl());
        resume.setDepartment(resumeUploadRequestDto.getDepartment());
        resume.setYearOfGraduation(resumeUploadRequestDto.getYearOfGraduation());
        user.setResume(resume);
        userRepositoryService.save(user);
        return ResponseEntity.ok(new ResponseDto("Resume uploaded successfully", user.getResume()));
    }
}
